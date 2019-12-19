package com.costello.dataflow;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.fs.EmptyMatchTreatment;
import org.apache.beam.sdk.options.*;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Reshuffle;
import org.apache.beam.sdk.transforms.Watch;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataflowApp {

    private static final Logger LOG = LoggerFactory.getLogger(DataflowApp.class);

    public static void main(String[] args) {
        DataflowAppOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(DataflowAppOptions.class);
        Pipeline pipeline = Pipeline.create(options);

        long s = System.currentTimeMillis();
        pipeline.apply(
                FileIO.match()
                        .withEmptyMatchTreatment(EmptyMatchTreatment.ALLOW)
                        .filepattern(options.getInputFilePattern())
                        .continuously(Duration.standardSeconds(10), Watch.Growth.never()))
                .apply(FileIO.readMatches())
                .apply(Reshuffle.viaRandomKey())
                .apply(ParDo.of(new FileToBytesDoFn()))
                .apply(Reshuffle.viaRandomKey())
                .apply("xml to POJO", ParDo.of(new XmlToPojoDoFnV2()));

        pipeline.run().waitUntilFinish();

        long e = System.currentTimeMillis();
        LOG.info("Parse time: {} ms ({} files/sec)", e - s, ((float) 5000 / (e - s) * 1000));
    }

    public interface DataflowAppOptions extends DataflowPipelineOptions, SdkHarnessOptions {
        @Validation.Required
        @Description("The GCS location of the file (files) you'd like to process")
        ValueProvider<String> getInputFilePattern();

        void setInputFilePattern(ValueProvider<String> value);
    }
}