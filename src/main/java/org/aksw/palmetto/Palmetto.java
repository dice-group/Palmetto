package org.aksw.palmetto;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;

import org.aksw.palmetto.calculations.CoherenceCalculation;
import org.aksw.palmetto.calculations.DifferenceBasedCoherenceCalculation;
import org.aksw.palmetto.calculations.UMassCoherenceCalculation;
import org.aksw.palmetto.corpus.CorpusAdapter;
import org.aksw.palmetto.corpus.lucene.IndexCreator;
import org.aksw.palmetto.corpus.lucene.LuceneCorpusAdapter;
import org.aksw.palmetto.io.DocumentTextSupplier;
import org.aksw.palmetto.io.MalletBasedDocumentTextSupplier;
import org.aksw.palmetto.io.SimpleDocumentTextSupplier;
import org.aksw.palmetto.io.SimpleWordSetReader;
import org.aksw.palmetto.prob.BooleanDocumentProbabilitySupplier;
import org.aksw.palmetto.subsets.AnyAny;
import org.aksw.palmetto.subsets.OneAll;
import org.aksw.palmetto.subsets.OneAny;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.OnePreceding;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Palmetto {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Palmetto.class);

    private static final String CMD = "palmetto --calcType <calcuation> --indexDir <directory> --inputFile <file> [OPTIONS...]";

    private static final String HELP_CMD = "help";
    private static final String INDEX_DIR_CMD = "indexDir";
    private static final String CALCULATION_TYPE_CMD = "calcType";
    private static final String INPUT_FILE_CMD = "inputFile";
    private static final String CORPUS_FILE_CMD = "corpusFile";
    private static final String MALLET_CORPUS_FILE_CMD = "malletImport";
    private static final String INDEX_FIELD_NAME_CMD = "indexFieldName";
    private static final String MIN_FREQUENCY_CMD = "minFreq";

    private static final String DEFAULT_INDEX_FIELD_NAME = "text";

    public static void main(String[] args) {
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        Options options = generateOptions();
        try {
            cmd = parser.parse(options, args, true);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(CMD, options);
            return;
        }
        if (cmd.hasOption(HELP_CMD)) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(CMD, options);
            return;
        }
        String indexPath = cmd.getOptionValue(INDEX_DIR_CMD);
        String calcType = cmd.getOptionValue(CALCULATION_TYPE_CMD);
        String inputFile = cmd.getOptionValue(INPUT_FILE_CMD);

        String fieldName = cmd.hasOption(INDEX_FIELD_NAME_CMD) ? cmd
                .getOptionValue(INDEX_FIELD_NAME_CMD)
                : DEFAULT_INDEX_FIELD_NAME;

        int minFrequency = -1;
        if (cmd.hasOption(MIN_FREQUENCY_CMD)) {
            try {
                minFrequency = Integer.parseInt(cmd.getOptionValue(MIN_FREQUENCY_CMD));
            } catch (NumberFormatException e) {
                LOGGER.error("Couldn't parse the minimum frequency. Aborting.", e);
            }
        }

        // index creation
        if (cmd.hasOption(CORPUS_FILE_CMD)) {
            String corpusFile = cmd.getOptionValue(CORPUS_FILE_CMD);
            DocumentTextSupplier supplier;
            if (cmd.hasOption(MALLET_CORPUS_FILE_CMD)) {
                supplier = MalletBasedDocumentTextSupplier.create(new File(corpusFile));
            } else {
                supplier = SimpleDocumentTextSupplier.create(new File(corpusFile));
            }
            if (supplier == null) {
                return;
            }
            IndexCreator creator = new IndexCreator(fieldName);
            if (!creator.createIndex(new File(indexPath), supplier)) {
                return;
            }
        }

        // coherence calculation
        CorpusAdapter adapter;
        try {
            adapter = LuceneCorpusAdapter.create(indexPath, fieldName);
        } catch (Exception e) {
            LOGGER.error(
                    "Caught an exception while opening lucene index. Aborting.",
                    e);
            return;
        }

        CoherenceCalculation calculator = getCoherenceCalculator(calcType);
        if (calculator == null) {
            LOGGER.error("Unknown calculation type \"" + calcType
                    + "\". Aborting");
            return;
        }

        SimpleWordSetReader reader = new SimpleWordSetReader();
        String wordsets[][] = reader.readWordSets(inputFile);
        LOGGER.info("Read " + wordsets.length + " from file.");

        BooleanDocumentProbabilitySupplier supplier = BooleanDocumentProbabilitySupplier
                .create(adapter);
        if (minFrequency > 0) {
            supplier.setMinFrequency(minFrequency);
        }

        double coherences[] = calculator
                .calculateCoherences(wordsets, supplier);

        printCoherences(coherences, wordsets, System.out);
    }

    private static void printCoherences(double[] coherences, String[][] wordsets, PrintStream out) {
        for (int i = 0; i < wordsets.length; i++) {
            out.format("%5d\t%3.5f\t%s%n", new Object[] { i, coherences[i], Arrays.toString(wordsets[i]) });
        }
    }

    private static CoherenceCalculation getCoherenceCalculator(String calcType) {
        String parts[] = calcType.toLowerCase().split("-");
        if ((parts.length < 1) || (parts.length > 2)) {
            return null;
        }
        if (parts.length == 1) {
            if (parts[0].equals("umass")) {
                return new UMassCoherenceCalculation();
            }
        }
        SubsetCreator creator;
        if (parts[0].equals("oneone")) {
            creator = new OneOne();
        } else if (parts[0].equals("onepreceding")) {
            creator = new OnePreceding();
        } else if (parts[0].equals("oneall")) {
            creator = new OneAll();
        } else if (parts[0].equals("oneany")) {
            creator = new OneAny();
        } else if (parts[0].equals("anyany")) {
            creator = new AnyAny();
        } else {
            return null;
        }
        if (parts[1].equals("diff")) {
            return new DifferenceBasedCoherenceCalculation(creator);
        } else {
            return null;
        }
    }

    @SuppressWarnings("static-access")
    private static Options generateOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt(HELP_CMD)
                .withDescription("print this message").create());
        options.addOption(OptionBuilder.withLongOpt(INDEX_DIR_CMD)
                .withDescription("The directory of the lucene index.").hasArg()
                .withArgName("directory").isRequired().create());
        options.addOption(OptionBuilder
                .withLongOpt(CALCULATION_TYPE_CMD)
                .withDescription(
                        "The coherence calculation type can be one of the following values: oneone-diff, oneall-diff, oneany-diff, anyany-diff, umass")
                .hasArg().withArgName("calcuation").isRequired().create());
        options.addOption(OptionBuilder
                .withLongOpt(INPUT_FILE_CMD)
                .withDescription(
                        "The file containing the top words of the topics (one topic per line).")
                .hasArg().withArgName("file").isRequired().create());
        options.addOption(OptionBuilder
                .withLongOpt(CORPUS_FILE_CMD)
                .withDescription(
                        "The file containing the corpus (one document per line). If this option is added, the lucene index will be created using this file.")
                .hasArg().withArgName("file").create());
        options.addOption(OptionBuilder
                .withLongOpt(INDEX_FIELD_NAME_CMD)
                .withDescription(
                        "The field name of the lucene index containing the words.")
                .hasArg().withArgName("name").create());
        options.addOption(OptionBuilder
                .withLongOpt(MIN_FREQUENCY_CMD)
                .withDescription(
                        "The minimum frequency a word must have. The default value is 10.")
                .hasArg().withArgName("min-frequency").create());
        options.addOption(OptionBuilder
                .withLongOpt(MALLET_CORPUS_FILE_CMD)
                .withDescription(
                        "The corpus file is a mallet import file. This option is only read if the " + CORPUS_FILE_CMD
                                + " is present.")
                .create());

        return options;
    }
}
