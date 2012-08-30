package calculs;

import java.util.*;
import javax.ejb.Stateful;
//import javax.ejb.Stateless;

@Stateful
public class CalculsBean implements CalculsRemote
{
	private LinkedList<Integer> _list = new LinkedList<Integer>();

	public void add(int n)
	{
		this._list.add(n);
	}

	public int sum()
	{
		int s = 0;
		for (int i : this._list) {
			s += i;
		}
		return s;
	}

	public int avg()
	{
		return this.sum() / this._list.size();
	}

	public int min()
	{
		int m = Integer.MAX_VALUE;
		for (int i : this._list) {
			if (i < m)
				m = i;
		}
		return m;
	}	

	public int max()
	{
		int m = Integer.MIN_VALUE;
		for (int i : this._list) {
			if (i > m)
				m = i;
		}
		return m;
	}
}

