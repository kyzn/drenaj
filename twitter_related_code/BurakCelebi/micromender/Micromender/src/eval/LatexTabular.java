package eval;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class LatexTabular {
	
	private String caption;
	private String label;
	
	private Map<String, String> columnTitleRsKeyMap;
	private List<Map<String, Object>> records;
	private String placement;
	
	private boolean isLongTable;

	LatexTabular(String caption, String label, List<Map<String, Object>> records, Map<String, String> columnTitleRsKeyMap, boolean isLongTable) {
		this(caption, label, records, columnTitleRsKeyMap, isLongTable, null);
	}
	
	LatexTabular(String caption, String label, List<Map<String, Object>> records, Map<String, String> columnTitleRsKeyMap, boolean isLongTable, String placement) {
		this.caption = caption;
		this.label = label;
		this.columnTitleRsKeyMap = columnTitleRsKeyMap;
		this.records = records;
		this.isLongTable = isLongTable;
		this.placement = placement==null ? "" : "["+placement+"]";
	}
	
	public List<Map<String, Object>> getRecords() {
		return records;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<String, String> getColumnTitleRsKeyMap() {
		return columnTitleRsKeyMap;
	}

	public void setColumnTitleRsKeyMap(Map<String, String> columnTitleRsKeyMap) {
		this.columnTitleRsKeyMap = columnTitleRsKeyMap;
	}
	
	public void setRecords(List<Map<String, Object>> records) {
		this.records = records;
	}

	public String getPlacement() {
		return placement;
	}

	public void setPlacement(String placement) {
		this.placement = placement;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();

		Set<Entry<String, String>> entrySet = columnTitleRsKeyMap.entrySet();
		int columns = entrySet.size();
		
		StringBuilder columnNames = new StringBuilder();
		for (Map.Entry<String, String> entry : entrySet) {
			
			if (isLongTable) {
				
				columnNames.append(" \\multicolumn{1}{")
				.append(columns==entrySet.size()? "l" : "c")
				.append("|}{\\textbf{").append(entry.getKey()).append("}} ");
				
			} else {
				columnNames.append("\\textbf{").append(entry.getKey()).append("}");
			}
			
			columns--;
			if (columns!=0) {
				columnNames.append(" & ").append(isLongTable ? "\n" : "");
			} else {
				columnNames.append("\n\\myLine\n");
			}
		}
		
		sb.append("%%%%%%%%%%%%%%%%\n");
		
		if(isLongTable) {
			
			sb.append("\\begin{center}\n")
			  .append("\\begin{longtable}{|l");
			
			for (int i = 1; i < entrySet.size(); i++) {
				sb.append("|c");
			}
			sb.append("|}\n")
			
			.append("%Here is the caption, the stuff in [] is the table of contents entry,\n")
			.append("%the stuff in {} is the title that will appear on the first page of the table.\n")
			.append("\\caption[" + caption + "]{" + caption + ".}\n")
			.append("\\label{" + label + "} \\\\\n");
			
			sb
			.append("%This is the header for the first page of the table...\n")
			.append("\\hline\n");
			
			sb.append(columnNames)
	    	  .append("\n\\endfirsthead\n\n");
			
			sb
			.append("%This is the header for the remaining page(s) of the table...\n")
			.append("\\multicolumn{")
			.append(entrySet.size())
			//.append("}{c}{{\\tablename} \\thetable{} -- Continued} \\\\\n")
			.append("}{c}{").append(caption).append(" -- Continued} \\\\\n")
			.append("  \\hline\n")
			.append(columnNames)
			.append("\n\\endhead\n\n");

			sb
			.append("%This is the footer for all pages except the last page of the table...\n")
			.append("  \\multicolumn{")
			.append(entrySet.size())
			.append("}{l}{{Continued on Next Page\\ldots}} \\\\\n")
			.append("\\endfoot\n\n")

			.append("%This is the footer for the last page of the table... \n")
			.append("\\endlastfoot\n\n");
			
			  
		  	appendTableValues(sb, entrySet);
		  	  
			  
			  sb.append("\n")
			    .append("\\end{longtable}\n")
			    .append("\\end{center}\n");
			    
		} else {
			
			sb.append("\\begin{table}" + placement + "\n")
			  .append("\\caption{").append(caption).append(".}\n")
			  .append("\\label{").append(label).append("}\n")
			  .append("\\centering\n")
			  .append("\\begin{tabular}{|l");
			
			
			for (int i = 1; i < entrySet.size(); i++) {
				sb.append("|c");
			}
			sb.append("|}\n")
			.append("\\hline\n");
			
			sb.append(columnNames);
			
			appendTableValues(sb, entrySet);
			
			sb.append("\\end{tabular}\n");
			sb.append("\\end{table}\n");
					
		}
		sb.append("%%%%%%%%%%%%%%%%");
	
		
		return sb.toString();
	}

	private void appendTableValues(StringBuilder sb, Set<Entry<String, String>> entrySet) {
		int columns;
		Collection<String> values = columnTitleRsKeyMap.values();
		
		  for (Map<String, Object> record : records) {
		
			columns = entrySet.size();
			for (String rsKey : values) {
				sb.append(String.valueOf(record.get(rsKey)).replace("_", "\\_"));
				
				columns--;
				if (columns!=0) {
					sb.append(" & ");
				}
			}
			 sb.append("\n")
			  .append("\\myLine\n");
		  }
	}

}
