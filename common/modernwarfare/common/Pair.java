package modernwarfare.common;

public class Pair
{
    private final Object left;
    private final Object right;

    public Pair(Object obj, Object obj1)
    {
        left = obj;
        right = obj1;
    }

    public Object getLeft()
    {
        return left;
    }

    public Object getRight()
    {
        return right;
    }

    public int hashCode()
    {
        return left.hashCode() ^ right.hashCode();
    }

    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (!(obj instanceof Pair))
        {
            return false;
        }
        else
        {
            Pair pair = (Pair)obj;
            return left.equals(pair.getLeft()) && right.equals(pair.getRight());
        }
    }
}
