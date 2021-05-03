package com.ykuee.datamaintenance.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.ykuee.datamaintenance.common.base.codeenum.BaseCodeEnum;
import com.ykuee.datamaintenance.common.base.exception.BusinessException;
import com.ykuee.datamaintenance.common.excel.ExcelCol;
import com.ykuee.datamaintenance.common.excel.ExcelHeader;
import com.ykuee.datamaintenance.constant.YesOrNo;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.sax.handler.RowHandler;

public class ExcelUtils {
	
	private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
	
	
	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";
	public static final String EMPTY = "";
	public static final String POINT = ".";

	private static List<List<Object>> lineList = new ArrayList<>();
	
	/**
	 * 
	  *<p>Title: exportExcel</p>
	  *<p>Description: 根据path生成excel文件 
	  *	  path: /home/test/xxx.xlsx   xls
	  *</p>
	  * @author Ykuee
	 * @param <T>
	  * @date 2021-3-10 
	  * @param list
	  * @param path
	 */
	public static <T> void exportExcel(List<T> list,String path,Class<T> clz) throws Exception {
		ExcelWriter writer = ExcelUtil.getBigWriter(path);
		exportExcel(writer,list,clz);
		 writer.close();
	}
	
	/**
	 * 
	  *<p>Title: exportExcel</p>
	  *<p>Description: 文件输出至流中
	  *  fileName: 前台下载显示的文件名  xxxx.xls  xxxx.xlsx
	  *</p>
	  * @author Ykuee
	 * @param <T>
	  * @date 2021-3-10 
	  * @param list
	  * @param response
	  * @param fileName
	  * @throws IOException
	 */
	public static <T> void exportExcel(List<T> list,HttpServletResponse response,String fileName,Class<T> clz) throws Exception {
		if(StrUtil.isBlank(fileName)) {
			fileName = "export.xlsx";
		}
		boolean xlsx = false;
		if (".xlsx".equals(fileName.substring(fileName.lastIndexOf(".")))) {
			xlsx =true;
        }
		ExcelWriter writer = ExcelUtil.getWriter(xlsx);
		exportExcel(writer,list,clz);
		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition","attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
			out = response.getOutputStream();
			writer.flush(out, true);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}finally {
			IoUtil.close(out);
			IoUtil.close(writer);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void exportExcel(ExcelWriter writer,List<T> list,Class<T> clz) throws Exception {
			String excelHeader = "";
			ArrayList<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
			boolean clzHasAnno = clz.isAnnotationPresent(ExcelHeader.class);
			if (clzHasAnno) {
				// 获取类上的注解
				ExcelHeader annotation = (ExcelHeader) clz.getAnnotation(ExcelHeader.class);
				// 输出注解上的属性
				excelHeader = annotation.value();
				logger.debug("clzName = {}, excelHeader = {}",clz.getName(),excelHeader);
			}
			Field[] fields = clz.getDeclaredFields();
			List<Field> fieldList = new ArrayList<>();
			for(Field f:fields){
				if(f.isAnnotationPresent(ExcelCol.class)){
					fieldList.add(f);
				}
			}
			fieldList.sort(Comparator.comparingInt(
					m -> m.getAnnotation(ExcelCol.class).order()
					));
			if(ObjectUtil.isNull(list) || list.size() == 0) {
				Map<String, Object> row = new LinkedHashMap<>();
				for(Field field : fieldList){
					ExcelCol fieldAnno = field.getAnnotation(ExcelCol.class);
					String excelCol = fieldAnno.value();
					if(StrUtil.isBlank(excelCol)) {
						row.put(field.getName(), "");
					}else {
						row.put(excelCol, "");
					}
				}
				rows.add(row);
			}else {
				for(Object o :list) {
					//Class clz = o.getClass();
					Map<String, Object> row = new LinkedHashMap<>();
					for(Field field : fieldList){
						ExcelCol fieldAnno = field.getAnnotation(ExcelCol.class);
						//输出注解属性
						String excelCol = fieldAnno.value();
						Object value = null;
						if (BaseCodeEnum.class.isAssignableFrom(field.getType())) {
							BaseCodeEnum enumValue = ReflectUtil.invoke(o, StrUtil.genGetter(field.getName()));
							if(ObjectUtil.isNotNull(enumValue)) {
								value = enumValue.getValue();
							}
						}else {
							value = ReflectUtil.invoke(o, StrUtil.genGetter(field.getName()));
							if(ObjectUtil.isNotNull(value) && fieldAnno.tureOrFalse()) {
								value = YesOrNo.YES.getKey().equals(value)?YesOrNo.YES.getValue():YesOrNo.NO.getValue();
							}
						}
						logger.debug("fieldName = {},value = {}, excelCol = {}",field.getName(),value, excelCol);
						if(StrUtil.isBlank(excelCol)) {
							row.put(field.getName(), value);
						}else {
							row.put(excelCol, value);
						}
					}
					rows.add(row);
				}
			}
			int rowSize = fieldList.size();
		    /* 自定义标题别名 */
		    try {
		    	SXSSFSheet sheet = (SXSSFSheet) writer.getSheet();
				sheet.trackAllColumnsForAutoSizing();
				for (int i = 0; i < rowSize; i++) {
					// 调整每一列宽度
					sheet.autoSizeColumn((short) i);
					// 解决自动设置列宽中文失效的问题
					sheet.setColumnWidth(i, sheet.getColumnWidth(i)*3);
				}
				
			} catch (Exception e) {
				logger.debug("导出为xlsx格式");
				XSSFSheet sheet = (XSSFSheet) writer.getSheet();
				for (int i = 0; i < rowSize; i++) {
					// 调整每一列宽度
					sheet.autoSizeColumn((short) i);
					// 解决自动设置列宽中文失效的问题
					sheet.setColumnWidth(i, sheet.getColumnWidth(i)*3);
				}
			}
		    writer.write(rows, true);
	}
	
    /**
     * excel导入工具类
     *
     * @param file       文件
     * @param columNames 列对应的字段名
     * @return 返回数据集合
     * @throws OperationException
     * @throws IOException
     */
    public static List<Map<String, Object>> importExcel(MultipartFile file, List<String> columNames) throws IOException {
    	if(ObjectUtil.isNull(file)) {
    		throw new BusinessException("500", "上传文件为空，请选择文件并上传");
    	}
    	String fileName = file.getOriginalFilename();
        // 上传文件名格式不正确
        if (fileName.lastIndexOf(".") != -1 && !".xlsx".equals(fileName.substring(fileName.lastIndexOf(".")))) {
            throw new BusinessException("500", "文件名格式不正确, 请使用后缀名为.xlsx的文件");
        }
        
        //读取数据
        ExcelUtil.readBySax(file.getInputStream(), 0, createRowHandler());
        //去除excel中的第一行数据
        lineList.remove(0);

        //将数据封装到list<Map>中
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < lineList.size(); i++) {
            if (null != lineList.get(i)) {
                Map<String, Object> hashMap = new HashMap<>();
                for (int j = 0; j < columNames.size(); j++) {
                    Object property = lineList.get(i).get(j);
                    hashMap.put(columNames.get(j), property);
                }
                dataList.add(hashMap);
            } else {
                break;
            }
        }
        return dataList;
    }
    
    private static RowHandler createRowHandler() {
        //清空一下集合中的数据
        lineList.removeAll(lineList);
        return new RowHandler() {
			@Override
			public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
				//将读取到的每一行数据放入到list集合中
				JSONArray jsonObject = new JSONArray(rowList);
				lineList.add(jsonObject.toList(Object.class));
			}
        };
    }
    
	public static <T> List importExcel(MultipartFile file, List<String> columNames,Class<T> clz) throws Exception {
    	if(ObjectUtil.isNull(file)) {
    		throw new BusinessException("500", "上传文件为空，请选择文件并上传");
    	}
    	String fileName = file.getOriginalFilename();
        // 上传文件名格式不正确
        if (fileName.lastIndexOf(".") != -1 && !".xlsx".equals(fileName.substring(fileName.lastIndexOf(".")))) {
            throw new BusinessException("500", "文件名格式不正确, 请使用后缀名为.xlsx的文件");
        }
        //读取数据
        ExcelUtil.readBySax(file.getInputStream(), 0, createRowHandler());
        //去除excel中的第一行数据
        lineList.remove(0);
        //将数据封装到list<Map>中
        
        List<T> objList = new ArrayList<T>();
        try {
	        for (int i = 0; i < lineList.size(); i++) {
	            if (null != lineList.get(i)) {
	                Object obj = clz.newInstance();
	                for (int j = 0; j < columNames.size(); j++) {
	                    Object property = lineList.get(i).get(j);
	                    if(!ObjectUtil.isEmpty(property)) {
	                    	Field field = ReflectUtil.getField(clz,columNames.get(j));
	                    	if (BaseCodeEnum.class.isAssignableFrom(field.getType())) {
	                    		property = EnumUtil.getEnumByValue(field.getType(),property);
							}
	                    	if(property instanceof String) {
		                    	property = StrUtil.trim((String)property);
		                    	if(YesOrNo.YES.getValue().equals(property)) {
		                    		property = YesOrNo.YES.getKey();
		                    	}
		                    	if(YesOrNo.NO.getValue().equals(property)) {
		                    		property = YesOrNo.NO.getKey();
		                    	}
		                    }
	                    	ReflectUtil.invoke(obj,StrUtil.genSetter(columNames.get(j)),ConvertUtils.convert(property,field.getType()));
	                    }
	                }
	                objList.add((T) obj);
	            } else {
	                break;
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("excel转换实体类发生异常");
			throw new BusinessException("导入数据发生异常，请使用导入模板导入数据！");
		}
        return objList;
	}
	
	
	/**
	 * 获得path的后缀名
	 * 
	 * @param path
	 * @return
	 */
	public static String getPostfix(String path) {
		if (path == null || EMPTY.equals(path.trim())) {
			return EMPTY;
		}
		if (path.contains(POINT)) {
			return path.substring(path.lastIndexOf(POINT) + 1, path.length());
		}
		return EMPTY;
	}
	
	
    
    /*
	private static <T> List test(List<String> columNames, Class<T> clz) {
		List<T> objList = new ArrayList<T>();
        try {
	        for (int i = 0; i < lineList.size(); i++) {
	            if (null != lineList.get(i)) {
	                Object obj = clz.newInstance();
	                for (int j = 0; j < columNames.size(); j++) {
	                    Object property = lineList.get(i).get(j);
	                    if(property instanceof String) {
	                    	property = StrUtil.trim((String)property);
	                    	if("是".equals(property)) {
	                    		property = "1";
	                    	}
	                		if("否".equals(property)) {
	                			property = "0";
	                    	}
	                    }
	                    if(!ObjectUtil.isEmpty(property)) {
	                    	Field field = ReflectUtil.getField(clz,columNames.get(j));
	                    	if (BaseCodeEnum.class.isAssignableFrom(field.getType())) {
	                    		property = getEnumByValue(field.getType(),property);
							}
	                    	ReflectUtil.invoke(obj,StrUtil.genSetter(columNames.get(j)),ConvertUtils.convert(property,field.getType()));
	                    }
	                }
	                objList.add((T) obj);
	            } else {
	                break;
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("excel转换实体类发生异常");
			throw new BusinessException("导入数据发生异常，请使用导入模板导入数据！");
		}
        return objList;
	}
	
    public static void main(String[] args) {
		System.out.println(getEnumByValue(Unit.class,"片"));
		List<String> columNames = new ArrayList<String>();
		columNames.add("name");
		columNames.add("unit");
		List<Object> s1 = new ArrayList<Object>(); 
		s1.add("test1");
		s1.add("片");
		List<Object> s2 = new ArrayList<Object>(); 
		s2.add("test2");
		s2.add("罐");
		lineList.add(s1);
		lineList.add(s2);
		List<AssistStdItemDTO> objList = test(columNames,AssistStdItemDTO.class);
		System.out.println(objList);
	}*/
	
}
