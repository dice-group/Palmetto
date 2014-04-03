package org.aksw.palmetto.prob;

import org.aksw.palmetto.corpus.SlidingWindowSupportingAdapter;
import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.SubsetDefinition;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public class BooleanSlidingWindowFrequencyDeterminer implements SlidingWindowFrequencyDeterminer {

    private SlidingWindowSupportingAdapter corpusAdapter;
    private int windowSize;
    private long wordSetCountSums[];

    public BooleanSlidingWindowFrequencyDeterminer(SlidingWindowSupportingAdapter corpusAdapter, int windowSize) {
        this.corpusAdapter = corpusAdapter;
        setWindowSize(windowSize);
    }

    @Override
    public CountedSubsets[] determineCounts(String[][] wordsets, SubsetDefinition[] definitions) {
        CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
        for (int i = 0; i < definitions.length; ++i) {
            countedSubsets[i] = new CountedSubsets(definitions[i].segments,
                    definitions[i].conditions, determineCounts(wordsets[i]));
        }
        return countedSubsets;
    }

    private int[] determineCounts(String wordset[]) {
        int counts[] = new int[(1 << wordset.length)];
        IntArrayList positions[];
        IntObjectOpenHashMap<IntArrayList[]> positionsInDocs = corpusAdapter.requestWordPositionsInDocuments(wordset);
        for (int i = 0; i < positionsInDocs.keys.length; ++i) {
            if (positionsInDocs.allocated[i]) {
                positions = ((IntArrayList[]) ((Object[]) positionsInDocs.values)[i]);
                addCountsFromDocument(positions, counts);
            }
        }
        return counts;
    }

    private void addCountsFromDocument(IntArrayList[] positions, int[] counts) {
        // Start from the first token in the document. Go through all tokens using the sliding window and add the counts
        // depending on every "new" token that enters the window.
        int posInList[] = new int[positions.length];
        int nextWordId = 0, minPosition = Integer.MAX_VALUE;
        // determine the first token which we should look at
        for (int i = 0; i < positions.length; ++i) {
            if ((positions[i] != null) && (positions[i].buffer[0] < minPosition)) {
                minPosition = positions[i].buffer[0];
                nextWordId = i;
            }
        }

        IntArrayList wordIdsInWindow = new IntArrayList(windowSize);
        IntArrayList wordPositionsInWindow = new IntArrayList(windowSize);
        long usedWindowWords;
        int lastWordId, posInWindow, firstTokenInNewWindow, wordInQueue, wordSetBits;
        while (minPosition < Integer.MAX_VALUE) {
            if (wordIdsInWindow.elementsCount > 0) {
                // add all combinations of the elements inside the window with the element found
                usedWindowWords = 1;
                while (usedWindowWords < (1 << wordIdsInWindow.elementsCount)) {
                    wordInQueue = 0;
                    wordSetBits = 0;
                    // Create the next possible word combination
                    while (wordInQueue < wordIdsInWindow.elementsCount) {
                        // If this word of the queue is part of this combination
                        if (((1 << wordInQueue) & usedWindowWords) > 0) {
                            // If this combinations contains two words with the same wordId --> discard it. This is done
                            // by setting the nextWordId-Bit inside the word, cause with this bit the combination will
                            // be sorted out by the next if after this loop
                            if ((wordSetBits & 1 << wordIdsInWindow.buffer[wordInQueue]) > 0) {
                                wordSetBits |= (1 << nextWordId);
                            } else {
                                wordSetBits |= 1 << wordIdsInWindow.buffer[wordInQueue];
                            }
                        }
                        ++wordInQueue;
                    }
                    // if the combination doesn't already contain the current word
                    if ((wordSetBits & (1 << nextWordId)) == 0) {
                        ++counts[wordSetBits | (1 << nextWordId)];
                    }
                    ++usedWindowWords;
                }
            }
            // don't forget the count for the single element found
            ++counts[1 << nextWordId];

            // We have finished this token. So increment the position
            lastWordId = nextWordId;
            ++posInList[lastWordId];
            // Find the next token we should look at
            minPosition = Integer.MAX_VALUE;
            for (int i = 0; i < positions.length; ++i) {
                if ((positions[i] != null) && (posInList[i] < positions[i].elementsCount)
                        && (positions[i].buffer[posInList[i]] < minPosition)) {
                    minPosition = positions[i].buffer[posInList[i]];
                    nextWordId = i;
                }
            }
            if (minPosition < Integer.MAX_VALUE) {
                firstTokenInNewWindow = positions[nextWordId].buffer[posInList[nextWordId]] - (windowSize - 1);
                // if the next token and the last one we found are inside a single window
                if ((positions[lastWordId].buffer[posInList[lastWordId] - 1]) >= firstTokenInNewWindow) {
                    // check the other words inside the queue
                    posInWindow = 0;
                    while ((posInWindow < wordPositionsInWindow.elementsCount)
                            && (wordPositionsInWindow.buffer[posInWindow] < firstTokenInNewWindow)) {
                        ++posInWindow;
                    }
                    // If there are tokens inside the window which have to be erased
                    if (posInWindow > 0) {
                        if (posInWindow == wordPositionsInWindow.elementsCount) {
                            wordPositionsInWindow.clear();
                            wordIdsInWindow.clear();
                        } else {
                            wordPositionsInWindow.elementsCount = wordPositionsInWindow.elementsCount - posInWindow;
                            System.arraycopy(wordPositionsInWindow.buffer, posInWindow, wordPositionsInWindow.buffer,
                                    0, wordPositionsInWindow.elementsCount);
                            wordIdsInWindow.elementsCount = wordPositionsInWindow.elementsCount;
                            System.arraycopy(wordIdsInWindow.buffer, posInWindow, wordIdsInWindow.buffer, 0,
                                    wordIdsInWindow.elementsCount);
                        }
                    }
                    // add the last word found and its position to the queue
                    wordIdsInWindow.add(lastWordId);
                    wordPositionsInWindow.add(positions[lastWordId].buffer[posInList[lastWordId] - 1]);
                } else {
                    if (wordPositionsInWindow.elementsCount > 0) {
                        wordPositionsInWindow.clear();
                        wordIdsInWindow.clear();
                    }
                }
            }
        }
    }

    @Override
    public void setWindowSize(int windowSize) {
        if (windowSize > Long.SIZE) {
            throw new IllegalArgumentException("This class only supports a window size up to " + Long.SIZE + ".");
        }
        this.windowSize = windowSize;
        determineWordSetCountSum();
    }

    @Override
    public long[] getCooccurrenceCounts() {
        return wordSetCountSums;
    }

    @Override
    public String getSlidingWindowModelName() {
        return "P_sw";
    }

    private boolean checkWordsInsideSingleWindow(int[] posInText) {
        int min = Integer.MAX_VALUE, max = -1;
        for (int i = 0; i < posInText.length; ++i) {
            if (posInText[i] > max) {
                max = posInText[i];
            }
            if (posInText[i] < min) {
                min = posInText[i];
            }
        }
        return (max - min) < this.windowSize;
    }

    protected void determineWordSetCountSum() {
        // For determining the sum of the counts we rely on a histogram of documents length and the window size
        wordSetCountSums = new long[this.windowSize];

        // create all needed factorials
        int factorials[] = new int[this.windowSize + 1];
        factorials[0] = 1;
        for (int i = 1; i < factorials.length; ++i) {
            factorials[i] = factorials[i - 1] * i;
        }

        // Go through the histogram, count the number of windows
        int numberOfDocumentStarts[] = new int[this.windowSize - 1];
        int numberOfWindowsInDocs = 0;
        int histogram[][] = corpusAdapter.getDocumentSizeHistogram();
        for (int i = 0; i < histogram.length; ++i) {
            // If this document is shorter than the window
            if (histogram[i][0] < (this.windowSize - 1)) {
                numberOfDocumentStarts[histogram[i][0] - 1] += histogram[i][1];
            } else {
                numberOfWindowsInDocs += histogram[i][1] * (histogram[i][0] - (this.windowSize - 1));
                numberOfDocumentStarts[this.windowSize - 2] += histogram[i][1];
            }
            // the word set with the size 1 is just the number of single tokens in the corpus
            wordSetCountSums[0] += histogram[i][1] * histogram[i][0];
        }

        // Determine how many word sets would have been counted using the number of windows
        for (int i = 1; i < wordSetCountSums.length; ++i) {
            wordSetCountSums[i] = numberOfWindowsInDocs * (factorials[this.windowSize - 1]
                    / (factorials[i] * factorials[this.windowSize - (i + 1)]));
        }

        // Determine how many additional word sets would have been counted at the beginning of the documents (where a
        // complete window can't be filled with the first words and we would have used a smaller window)
        for (int i = numberOfDocumentStarts.length - 1; i >= 0; --i) {
            // i is the number of words at the beginning of the document
            // go through the wordsets which have a length that is smaller or equal to the current (smaller) window
            for (int j = 1; j <= i; ++j) {
                wordSetCountSums[j] += numberOfDocumentStarts[i] * (factorials[i]
                        / (factorials[j] * factorials[i - j]));
            }
            // all documents that had this size, would also have a smaller size
            if (i > 0) {
                numberOfDocumentStarts[i - 1] += numberOfDocumentStarts[i];
            }
        }
    }

    protected int determineCount(IntArrayList[] positions) {
        for (int i = 0; i < positions.length; ++i) {
            if ((positions[i] == null) || (positions[i].size() == 0)) {
                return 0;
            }
        }
        int posInList[] = new int[positions.length];
        int posInText[] = new int[positions.length];
        // determine the first positions of the words
        for (int i = 0; i < positions.length; ++i) {
            posInText[i] = positions[i].buffer[0];
        }
        // Go through all combinations of words and test if they are inside a single window
        int currentWordId, count = 0;
        while (posInList[0] < positions[0].size()) {
            if (checkWordsInsideSingleWindow(posInText)) {
                ++count;
            }
            currentWordId = positions.length - 1;
            ++posInList[currentWordId];
            while ((currentWordId > 0) && (posInList[currentWordId] >= positions[currentWordId].size())) {
                posInList[currentWordId] = 0;
                posInText[currentWordId] = positions[currentWordId].buffer[0];
                --currentWordId;
                ++posInList[currentWordId];
            }
            if (posInList[currentWordId] < positions[currentWordId].size()) {
                posInText[currentWordId] = positions[currentWordId].buffer[posInList[currentWordId]];
            }
        }
        return count;
    }

}
