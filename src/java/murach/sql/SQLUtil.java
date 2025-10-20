package murach.sql;

import java.sql.*;

public class SQLUtil
{
    public static String getHtmlTable(ResultSet results)
            throws SQLException
    {
        StringBuilder htmlTable = new StringBuilder();
        ResultSetMetaData metaData = results.getMetaData();
        int columnCount = metaData.getColumnCount();

        htmlTable.append("<table border='1'>");

        // header
        htmlTable.append("<tr>");
        for (int i = 1; i <= columnCount; i++) {
            htmlTable.append("<th>")
                     .append(metaData.getColumnName(i))
                     .append("</th>");
        }
        htmlTable.append("</tr>");

        // rows
        while (results.next()) {
            htmlTable.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                Object value;
                try {
                    value = results.getObject(i);
                } catch (ClassCastException e) {
                    // fallback nếu driver ép kiểu sai
                    value = results.getString(i);
                }

                // ép kiểu an toàn
                if (value instanceof java.math.BigInteger) {
                    value = ((java.math.BigInteger) value).longValue(); // ép về long
                } else if (value instanceof java.math.BigDecimal) {
                    value = ((java.math.BigDecimal) value).toPlainString();
                }

                htmlTable.append("<td>")
                         .append(value != null ? value : "NULL")
                         .append("</td>");
            }
            htmlTable.append("</tr>");
        }

        htmlTable.append("</table>");
        return htmlTable.toString();
    }
}