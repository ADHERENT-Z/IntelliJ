package zyw.adapter;

public class VlcPlayer implements AdvancedMediaPlayer{

    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file. Name: "+ fileName);
    }

    // 接口适配
    @Override
    public void playMp4(String fileName) {
        //什么也不做
    }
}
