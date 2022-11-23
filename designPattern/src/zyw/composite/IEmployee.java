package zyw.composite;

import java.util.List;

public interface IEmployee {

    public void add(IEmployee e);
    public void remove(IEmployee e);
    public List<IEmployee> getSubordinates();

}
