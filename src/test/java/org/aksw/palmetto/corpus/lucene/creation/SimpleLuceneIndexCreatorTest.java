package org.aksw.palmetto.corpus.lucene.creation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import junit.framework.Assert;

import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.corpus.lucene.LuceneCorpusAdapter;
import org.apache.lucene.index.CorruptIndexException;
import org.junit.Test;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

public class SimpleLuceneIndexCreatorTest {

    public static String DOCUMENTS[] = { "This is a test document.", "This is another test document.",
            "This is a third one." };
    public static String TEST_WORDS[] = { "is", "document", "dog" };
    public static int EXPECTED_DOC_COUNTS[] = new int[] { 3, 2, 0 };

    @Test
    public void test() throws CorruptIndexException, IOException {
        File indexDir = createTempDirectory();
        Iterator<String> docIterator = Arrays.asList(DOCUMENTS).iterator();
        // create the index
        SimpleLuceneIndexCreator creator = new SimpleLuceneIndexCreator(Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME);
        Assert.assertTrue(creator.createIndex(indexDir, docIterator));

        // test the created index
        // create an adapter
        LuceneCorpusAdapter adapter = LuceneCorpusAdapter.create(indexDir.getAbsolutePath(),
                Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME);
        // query the test words
        ObjectObjectOpenHashMap<String, IntArrayList> wordDocMapping = new ObjectObjectOpenHashMap<String, IntArrayList>();
        for (int i = 0; i < TEST_WORDS.length; ++i) {
            wordDocMapping.put(TEST_WORDS[i], new IntArrayList());
        }
        adapter.getDocumentsWithWords(wordDocMapping);
        // compare the result with the expected counts
        int retrievedDocs;
        for (int i = 0; i < TEST_WORDS.length; ++i) {
            retrievedDocs = wordDocMapping.get(TEST_WORDS[i]).elementsCount;
            Assert.assertEquals("Expected " + EXPECTED_DOC_COUNTS[i] + " documents containing the word \""
                    + TEST_WORDS[i] + "\", but got " + retrievedDocs + " documents form the index.",
                    EXPECTED_DOC_COUNTS[i], retrievedDocs);
        }
    }

    public static File createTempDirectory()
            throws IOException {
        File temp = File.createTempFile("temp_index", Long.toString(System.nanoTime()));
        if (!(temp.delete())) {
            return null;
        }
        if (!(temp.mkdir())) {
            return null;
        }
        return temp;
    }

}
