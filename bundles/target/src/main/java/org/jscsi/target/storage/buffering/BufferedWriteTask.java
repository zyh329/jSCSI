package org.jscsi.target.storage.buffering;

/**
 * BufferedWriteTasks are used to
 * keep the information of a write request
 * for later storaging in TreeTank.
 * 
 * @author Andreas Rain
 * 
 */
public class BufferedWriteTask {

    /**
     * The bytes to buffer.
     */
    private final byte[] mBytes;

    /**
     * Where to start writing in the storage.
     */
    private final long mStorageIndex;

    /**
     * All these parameters are passed to the {@link TreetankStorageModule} write method
     * and are being buffered using this class and its constructor.
     * 
     * @param pBytes
     * @param pStorageIndex
     */
    public BufferedWriteTask(byte[] pBytes, long pStorageIndex) {
        super();
        this.mBytes = pBytes;
        this.mStorageIndex = pStorageIndex;
    }

    /**
     * 
     * @return byte[] - the bytes buffered in this task.
     */
    public byte[] getBytes() {
        return mBytes;
    }

    /**
     * 
     * @return long - the storageindex
     */
    public long getStorageIndex() {
        return mStorageIndex;
    }

    public static class PoisonTask extends BufferedWriteTask {

        /**
         * Constructor.
         * 
         * @param pBytes
         * @param pOffset
         * @param pLength
         * @param pStorageIndex
         */
        public PoisonTask() {
            super(new byte[0], -1);
        }

    }
}