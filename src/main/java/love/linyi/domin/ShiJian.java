package love.linyi.domin;

import java.time.LocalDateTime;

public class ShiJian {
    LocalDateTime otime;
    int distance;
    public LocalDateTime getOtime() {
        return otime;
    }
    public void setOtime(LocalDateTime otime) {
        this.otime = otime;
    }
    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "ShiJian{" +
                "otime='" + otime + '\'' +
                ", distance=" + distance +
                ","+
                '}';
    }
}
