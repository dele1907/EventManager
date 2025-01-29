package helper;

public class ListFormattingHelper {
    public final static int ROW_WIDTH = 20; //Standard Row-Width
    public final static int ROW_WIDTH_AFTER_EMAIL = 30; //Standard Row-Width after eMail (bigger because emailAddresses are long)

    /**
     * <h3>Set Row Format</h3>
     * @param row the amount of rows you want to show
     * @param eMailPosition the position of the eMail
     * @see StringBuilder StringBuilder
     */

    public static String setRowFormat(int row, int eMailPosition) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i<row; i++) {

            if (i == eMailPosition-1) {

                sb.append("%-").append(ROW_WIDTH_AFTER_EMAIL).append("s");
            } else if (i == row - 1) {

                sb.append("%-").append(ROW_WIDTH).append("s%n");
            } else {

                sb.append("%-").append(ROW_WIDTH).append("s");
            }
        }

        return sb.toString();
    }

}
