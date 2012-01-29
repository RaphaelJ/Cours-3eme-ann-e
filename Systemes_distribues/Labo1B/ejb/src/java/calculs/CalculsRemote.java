package calculs;

import javax.ejb.Remote;

@Remote
public interface CalculsRemote
{
    public void add(int n);
    public int sum();
    public int avg();
    public int min();
    public int max();
}

