package nl.codecentric.xpdays2017.gareth.definition;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.craftsmenlabs.gareth.api.annotation.Assume;
import org.craftsmenlabs.gareth.api.annotation.Baseline;
import org.craftsmenlabs.gareth.api.annotation.Time;
import org.craftsmenlabs.gareth.api.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Created by hylke on 22/05/2017.
 */
public class BicycleShedDefinition {


    @Baseline(glueLine = "Get current count sold products")
    public void baseline(final Storage storage) {
        int onDisplaySoldItems = getOnDisplaySoldItems();

        storage.store("onDisplay", onDisplaySoldItems);

    }

    @Assume(glueLine = "Number of displayed products sold is 10")
    public void assume(final Storage storage) {
        int baselineOnDisplay = storage.get("onDisplay", Integer.class).get();
        int currentOnDisplay = getOnDisplaySoldItems();
        if (currentOnDisplay - baselineOnDisplay >= 10) {
            // Success
        }

    }


    @Time(glueLine = "1 Minute")
    public Duration time() {
        return Duration.of(1L, ChronoUnit.MINUTES);
    }

    private int getOnDisplaySoldItems() {
        int onDisplayCount = 0;
        try {
            final ObjectMapper objectMapper = new ObjectMapper();

            final File salesFile = new File(FileUtils.getTempDirectory(), "bicycle-shed-sales.txt");
            for (final String line : FileUtils.readLines(salesFile, Charset.defaultCharset())) {
                final JsonNode jsonNode = objectMapper.readValue(line, JsonNode.class);
                if (jsonNode.get("onDisplay").booleanValue()) {
                    onDisplayCount++;
                }
            }
        } catch (final IOException e) {
        }

        System.out.println(String.format("On display items: %d", onDisplayCount));

        return onDisplayCount;
    }


}
