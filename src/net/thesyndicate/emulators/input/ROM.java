package net.thesyndicate.emulators.input;

import net.thesyndicate.emulators.exception.InvalidFilePathException;
import net.thesyndicate.emulators.exception.MemoryOutOfBoundsException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ROM {

    private ByteBuffer buffer;
    private String fileName;

    private ROM(String fileName) {
        buffer = ByteBuffer.allocate(4096);
        try {
            File file = new File(fileName);
            this.fileName = file.getName();
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

    public String getFileName() {
        return fileName;
    }

    /***
     * Generic hash string generating method.
     * Citation: http://www.codejava.net/coding/how-to-calculate-md5-and-sha-hash-values-in-java
     * @param algorithm the algorithm used to create the hash
     * @return hash string
     */
    private String hashString(String algorithm) {
        byte[] hashedBytes = null;

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            hashedBytes = digest.digest(buffer.array());

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return convertByteArrayToHexString(hashedBytes);
    }

    /***
     * Convert ByteArray into a Hex String.
     * Citation: http://www.codejava.net/coding/how-to-calculate-md5-and-sha-hash-values-in-java
     * @param arrayBytes array of byte values
     * @return hex string
     */
    private String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xFF) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }

    /***
     * Generates a MD5 hash string of the ROM data
     * Citation: http://www.codejava.net/coding/how-to-calculate-md5-and-sha-hash-values-in-java
     * @return MD5 hash string
     */
    public String generateMD5() {
        return hashString("MD5");
    }

    /***
     * Generates a SHA-1 hash string of the ROM data
     * Citation: http://www.codejava.net/coding/how-to-calculate-md5-and-sha-hash-values-in-java
     * @return SHA-1 hash string
     */
    public String generateSHA1() {
        return hashString("SHA-1");
    }

    /***
     * Generates a SHA-256 hash string of the ROM data
     * Citation: http://www.codejava.net/coding/how-to-calculate-md5-and-sha-hash-values-in-java
     * @return SHA-256 hash string
     */
    public String generateSHA256() {
        return hashString("SHA-256");
    }
}
