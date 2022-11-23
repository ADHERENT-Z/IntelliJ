package randomAccess;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DataIO {
    /**
     * 从输入流中读取字符，直至读入 size 个码元
     * 或者遇到具有 0 值的字符，然后跳过输入字段剩余 0 值
     *
     * @param size
     * @param in
     * @return
     * @throws IOException
     */
    public static String readFixedString(int size, DataInput in)
            throws IOException {

        StringBuilder b = new StringBuilder(size);
        int i = 0;
        boolean done = false;
        while (!done && i < size){
            char ch = in.readChar();
            i++;
            if (ch == 0)
                done = true;
            else
                b.append(ch);
        }
        // 跳过输入字段中剩余的 0 值
        in.skipBytes(2 * (size - i));
        return b.toString();
    }

    /**
     * 写出从字符串开头开始指定数量的码元
     * 如果码元过少，用 0 值补齐字符串
     *
     * @param s
     * @param size
     * @param out
     * @throws IOException
     */
    public static void writeFixedString(String s, int size, DataOutput out) throws IOException
    {
        for (int i = 0; i < size; i++)
        {
            char ch = 0;
            if (i < s.length())
                ch = s.charAt(i);
            out.writeChar(ch);
        }
    }
}
