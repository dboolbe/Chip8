package net.thesyndicate.emulators.input;

import net.thesyndicate.emulators.exception.InvalidFilePathException;
import net.thesyndicate.emulators.exception.MemoryOutOfBoundsException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ROM {

    private ByteBuffer buffer;
    private String fileName;

    private ROM(String fileName) {
        this.fileName = fileName;

        buffer = ByteBuffer.allocate(4096);
        try {
            File file = new File(fileName);
            if(!file.isFile())
                throw new InvalidFilePathException("Not a valid file name: " + fileName);

            // checks if file is within memory limits
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] bytes = new byte[64];
            while(fileInputStream.read(bytes, 0, bytes.length) > 0)
                buffer.put(bytes);

            // switch byteBuffer to read mode
            buffer.flip();

            if(buffer.limit() >= (0xFFF - 0x200))
                throw new MemoryOutOfBoundsException("Program Out Of Memory Bounds: " + buffer.limit());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFilePathException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MemoryOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static ROM createROM(String fileName) {
        return new ROM(fileName);
    }

    public ByteBuffer getBuffer() {
        ByteBuffer clone = ByteBuffer.allocate(buffer.capacity());
        buffer.rewind();
        clone.put(buffer);
        buffer.rewind();
        clone.flip();
        return clone;
    }
}
