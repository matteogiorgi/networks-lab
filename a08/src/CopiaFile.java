public class CopiaFile
{
    private final String name;
    private final int temp;

    public CopiaFile(String name, int temp)
    {
        this.name = name;
        this.temp = temp;
    }

    public String name()
    {
        return name;
    }

    public int temp()
    {
        return temp;
    }

    @Override
    public String toString()
    {
        return name + ": " + temp + " nsec";
    }
}
