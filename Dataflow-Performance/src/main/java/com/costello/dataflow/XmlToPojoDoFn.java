package com.costello.dataflow;

import com.costello.dataflow.model.LogEntry;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.transforms.DoFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class XmlToPojoDoFn extends DoFn<FileIO.ReadableFile, LogEntry> {

    private static final Logger LOG = LoggerFactory.getLogger(XmlToPojoDoFn.class);

    @ProcessElement
    public void process(@Element FileIO.ReadableFile readableFile, OutputReceiver<LogEntry> out) {
        ObjectMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            LogEntry logEntry = mapper.readValue(readableFile.readFullyAsBytes(), LogEntry.class);
            out.output(logEntry);
        } catch (IOException e) {
            LOG.error("Unable to process stream", e);
        }
    }
}