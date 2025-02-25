package e1;

public class StringUtilities
{
    public static boolean isValidString (String string, String valid_characters, int size)
    {
        if (string == null || string.isEmpty() || string.length() < size) return false;

        int i;

        //verifies the rest of requirements
        for (i = 0; i < string.length(); ++i)
            if (!Character.isDigit(string.charAt(i)) && !valid_characters.contains(String.valueOf(string.charAt(i)))) return false;

        return true;
    }

    public static String lowercaseFirst(String string)
    {
        int i;
        StringBuilder newString = new StringBuilder();
        StringBuilder uppercaseString = new StringBuilder();

        //concatenates lowercase and uppercase characters to them corresponding string
        for (i = 0; i<string.length(); ++i)
        {
            if (Character.isLowerCase(string.charAt(i)))
                newString.append(string.charAt(i));
            else
                uppercaseString.append(string.charAt(i));
        }

        //concatenates the lowercase after the uppercase
        newString.append(uppercaseString);
        return newString.toString();
    }

    public static boolean checkTextStats (String string, int min, int max)
    {
        //if the string is not valid throws illegal exception
        if (string == null || string.isEmpty() || min<=0 || max<=0) throw new IllegalArgumentException();

        //stores the words of the string into an array of strings
        String[] split_string = string.split("\\s+");
        String max_string = "";
        double totalsize = 0, split_len = split_string.length, split_this_len, avg;
        int i;

        //stores the total number of characters and the bigger string
        for (i = 0; i < (split_len); ++i)
        {
            totalsize += (split_this_len = split_string[i].length());
            if (split_this_len > max_string.length())
                max_string = split_string[i];
        }

        //calculates the average string size and returns true if the conditions were satisfied and false if they were not
        avg = totalsize/split_len;
        return avg >= min && avg <= max && max_string.length() <= 2*avg;
    }
}