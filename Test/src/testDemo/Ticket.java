package testDemo;

import java.util.Date;

public class Ticket {
    private String station;
    private String destination;
    private Date time;

    public Ticket(String station, String destination, Date time) {
        this.station = station;
        this.destination = destination;
        this.time = time;
    }

    @Override
    public String toString() {
        return "票根：【始发站: " + station + "----> 终点： '" +
                destination + ", 出发时间：" + time + '】';
    }

}
