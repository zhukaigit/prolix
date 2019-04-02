package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jxl.Cell;
import jxl.CellType;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@SuppressWarnings("all")
public class XlsUtil {

  public static final String exportExcel(String[] Title, List<?> listContent, OutputStream os) {
    String result = "export excel successfully";
    try {
      WritableWorkbook workbook = Workbook.createWorkbook(os);

      WritableSheet sheet = workbook.createSheet("Sheet1", 0);

      SheetSettings sheetset = sheet.getSettings();
      sheetset.setProtected(false);

      WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
      WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

      WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
      wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
      wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
      wcf_center.setAlignment(Alignment.CENTRE);
      wcf_center.setWrap(false);

      WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
      wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN);
      wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE);
      wcf_left.setAlignment(Alignment.LEFT);
      wcf_left.setWrap(false);

      for (int i = 0; i < Title.length; i++) {
        sheet.addCell(new Label(i, 0, Title[i], wcf_center));
      }

      Field[] fields = null;
      int i = 1;
      for (Iterator i$ = listContent.iterator(); i$.hasNext(); ) {
        Object obj = i$.next();
        fields = obj.getClass().getDeclaredFields();
        int j = 0;
        for (Field v : fields) {
          v.setAccessible(true);
          Object va = v.get(obj);
          if (va == null) {
            va = "";
          }
          sheet.addCell(new Label(j, i, va.toString(), wcf_left));
          j++;
        }
        i++;
      }

      workbook.write();

      workbook.close();
    } catch (Exception e) {
      result = "export excel exception, errorMsg：" + e.toString();
      e.printStackTrace();
    }
    return result;
  }

  public static final String exportExcel(String fileName, String[] Title, List<?> listContent) {
    String result = "export excel successfully";
    try {
      WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));

      WritableSheet sheet = workbook.createSheet("Sheet1", 0);

      SheetSettings sheetset = sheet.getSettings();
      sheetset.setProtected(false);

      WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
      WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

      WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
      wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
      wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
      wcf_center.setAlignment(Alignment.CENTRE);
      wcf_center.setWrap(false);

      WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
      wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN);
      wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE);
      wcf_left.setAlignment(Alignment.LEFT);
      wcf_left.setWrap(false);

      for (int i = 0; i < Title.length; i++) {
        sheet.addCell(new Label(i, 0, Title[i], wcf_center));
      }

      Field[] fields = null;
      int i = 1;
      for (Iterator i$ = listContent.iterator(); i$.hasNext(); ) {
        Object obj = i$.next();
        fields = obj.getClass().getDeclaredFields();
        int j = 0;
        for (Field v : fields) {
          v.setAccessible(true);
          Object va = v.get(obj);
          if (va == null) {
            va = "";
          }
          sheet.addCell(new Label(j, i, va.toString(), wcf_left));
          j++;
        }
        i++;
      }

      workbook.write();

      workbook.close();
    } catch (Exception e) {
      result = "export excel exception, errorMsg：" + e.toString();
      e.printStackTrace();
    }
    return result;
  }

  public static List<Object[]> readExcel(String filepath) {
    List list = new ArrayList();
    try {
      InputStream is = new FileInputStream(filepath);
      Workbook rwb = Workbook.getWorkbook(is);

      Sheet[] sheets = rwb.getSheets();
      for (int s = 0; s < sheets.length; s++) {
        Sheet st = sheets[s];
        int rs = st.getRows();
        for (int r = 0; r < rs; r++) {
          Cell[] cells = st.getRow(r);
          Object[] objs = new Object[cells.length];
          for (int c = 0; c < cells.length; c++) {
            if (cells[c].getType().equals(CellType.LABEL)) {
              LabelCell labelc00 = (LabelCell) cells[c];
              objs[c] = labelc00.getString();
            } else {
              objs[c] = cells[c].getContents();
            }
          }
          list.add(objs);
        }
      }

      rwb.close();
      return list;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static List<Object[]> readExcel(InputStream is) {
    List list = new ArrayList();
    try {
      Workbook rwb = Workbook.getWorkbook(is);
      Sheet[] sheets = rwb.getSheets();
      for (int s = 0; s < sheets.length; s++) {
        Sheet st = sheets[s];
        int rs = st.getRows();
        for (int r = 0; r < rs; r++) {
          Cell[] cells = st.getRow(r);
          Object[] objs = new Object[cells.length];
          for (int c = 0; c < cells.length; c++) {
            if (cells[c].getType().equals(CellType.LABEL)) {
              LabelCell labelc00 = (LabelCell) cells[c];
              objs[c] = labelc00.getString();
            } else {
              objs[c] = cells[c].getContents();
            }
          }
          list.add(objs);
        }
      }

      rwb.close();
      return list;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


}

