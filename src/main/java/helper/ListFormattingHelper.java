package helper;


public class ListFormattingHelper {
    public final static int ROW_WIDTH = 20;
    public final static int ROW_WIDTH_AFTER_EMAIL = 30;;

    public static String rowFormat(int row, int emailPosition) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i<row; i++) {


            if (i == emailPosition-1) {

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
