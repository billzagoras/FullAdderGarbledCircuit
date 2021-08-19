package yao.gate;

import java.util.BitSet;
import java.util.Random;

public class Wire
{
    private
    BitSet encoding;
    static int AES_128 = 128;
    final int INNUM = 129;
    final int OUTNUM = 128;
    static String ALGORITHM = "AES";
    static String AES_CBC_NO_PADDING = "AES/CBC/NoPadding";

    protected Wire() throws Exception
    {
        this.encoding = new BitSet(INNUM);
        randomizeBitSet();
        paintBitSet();
    }

    protected Wire(boolean flag) throws Exception
    {
        this.encoding = new BitSet(INNUM);
        randomizeBitSet();
        paintBitSet(flag);
    }

    protected Wire(Wire wire) throws Exception
    {
        this.encoding = new BitSet(INNUM);
        stealBitSet(wire);
        paintBitSet();
    }

    protected Wire(Wire wire, boolean flag) throws Exception
    {
        this.encoding = new BitSet(INNUM);
        stealBitSet(wire);
        paintBitSet(flag);
    }

    protected BitSet getEncoding()
    {
        return this.encoding;
    }

    //Randomize the encoding of 0 and 1 binary representation
    private void randomizeBitSet()
    {
        int tmp;
        Random rand = new Random();
        for(int i = 0; i < INNUM; i++)
        {
            tmp = rand.nextInt(2);
            if(tmp == 1)
                this.encoding.set(i);
        }
    }


    //Overwrite the existing encoding of 0 and 1 binary representation
    private void stealBitSet(Wire wire)
    {
        for(int i = 0; i < INNUM; i++)
        {
            if (wire.getEncoding().get(i))
                this.encoding.set(i);
            else
                this.encoding.clear(i);
        }
    }

    //Randomize the last bit of the encoding of 0 and 1 binary representation
    private void paintBitSet()
    {
        Random rand = new Random();
        int tmp = rand.nextInt(2);
        if (tmp == 1)
            this.encoding.set(INNUM);
        else
            this.encoding.clear(INNUM);
    }

    //Overloading: Randomize the last bit of the encoding of 0 and 1 binary representation for ~wire
    private void paintBitSet(boolean flag)
    {
        if (flag)
            this.encoding.set(INNUM);
        else
            this.encoding.clear(INNUM);
    }
}