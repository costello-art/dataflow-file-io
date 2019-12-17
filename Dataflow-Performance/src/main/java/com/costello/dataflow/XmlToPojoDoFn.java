package com.costello.dataflow;

import com.costello.dataflow.model.LogEntry;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.io.fs.MatchResult;
import org.apache.beam.sdk.transforms.DoFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;

import static java.nio.channels.Channels.newInputStream;

public class XmlToPojoDoFn extends DoFn<MatchResult.Metadata, LogEntry> {

    private static final Logger LOG = LoggerFactory.getLogger(XmlToPojoDoFn.class);

    @ProcessElement
    public void process(@Element MatchResult.Metadata metadata, OutputReceiver<LogEntry> out) {
        ObjectMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String resourcePath = metadata.resourceId().toString();

        try (ReadableByteChannel chan = FileSystems.open(FileSystems.matchNewResource(resourcePath, false));
             InputStream inputStream = newInputStream(chan)) {

            LogEntry logEntry = mapper.readValue(inputStream, LogEntry.class);

            out.output(logEntry);
        } catch (IOException e) {
            LOG.error("Unable to process stream", e);
        }
    }
}