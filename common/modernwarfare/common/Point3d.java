package modernwarfare.common;

public class Point3d
{
    public Object x;
    public Object y;
    public Object z;

    public Point3d(Object obj, Object obj1, Object obj2)
    {
        x = obj;
        y = obj1;
        z = obj2;
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof Point3d))
        {
            return false;
        }
        else
        {
            Point3d point3d = (Point3d)obj;
            return x == point3d.x && y == point3d.y && z == point3d.z;
        }
    }

    public int hashCode()
    {
        int i = 1;
        i = i * 31 + x.hashCode();
        i = i * 31 + y.hashCode();
        i = i * 31 + z.hashCode();
        return i;
    }
}
