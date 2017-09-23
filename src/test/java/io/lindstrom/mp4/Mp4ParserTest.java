package io.lindstrom.mp4;

import io.lindstrom.mp4.box.Box;
import io.lindstrom.mp4.box.GenericContainerBox;
import io.lindstrom.mp4.box.iso.FileTypeBox;
import org.junit.Test;

import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class Mp4ParserTest {
    @Test
    public void parse() throws Exception {
        Path input = Paths.get("src/test/resources/v1-init.mp4");
        //Path input = Paths.get("src/test/resources/v6-25539.mp4");
        Path output = Paths.get("out.mp4");

        Mp4Parser mp4Parser = new Mp4Parser();

        Container container;

        try (ReadableByteChannel channel = Files.newByteChannel(input)) {
            container = mp4Parser.parse(channel);
        }

       // prettyPrint(container.getBoxes(), 0);

        Box b = container.findBox(FileTypeBox.class).get();
        System.out.println(b);
        try (WritableByteChannel channel = Files.newByteChannel(output,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            mp4Parser.writeBoxes(container.getBoxes(), channel);
        }

        assertArrayEquals(Files.readAllBytes(input), Files.readAllBytes(output));
    }

    private void prettyPrint(List<Box> boxes, int level) {
        String prefix = level > 0 ? String.format("%" + level + "s", "") : "";
        for (Box box : boxes) {
            System.out.println(prefix + box.toString());
            if (box instanceof GenericContainerBox) {
                prettyPrint(((GenericContainerBox) box).getBoxes(), level + 2);
            }
        }
    }

}