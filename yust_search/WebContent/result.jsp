<%@ page import="java.util.*" import="org.entity.Document"
	import="java.util.regex.*" language="java"
	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Yust Search</title>
</head>
<body>
	<form method="POST" action="Bridge">
		<table width="300" bgcolor="#7FFF00">
			<tr>
				<%
					String query_input = (String) request.getAttribute("query_input");
				%>
				<td><INPUT TYPE="TEXT" NAME="query" value="<%=query_input%>" ></td>
				<td><INPUT TYPE="SUBMIT" VALUE="搜一下"></td>
			</tr>
		</table>
		<br> <br>
	</form>
	<p>
		<%
			org.entity.Document doc = (org.entity.Document) request
					.getAttribute("im");
		%>
		<%!//消除重复 (查询到的query)函数
	public static String[] array_unique(String[] a) {
		// array_unique  
		List<String> list = new LinkedList<String>();
		for (int i = 0; i < a.length; i++) {
			if (!list.contains(a[i])) {
				list.add(a[i]);
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
	}%>

		<%!//用增则表达式获取<em>xxx</em>里的query数据。
	public static String[] getHighlightWord(String html) {
		// String html = "<ul><li>1.hehe</li><li>2.hi</li><li>3.hei</li></ul>";
		String ss = "<em>([\\s\\S]*?)</em>";
		Pattern pa = Pattern.compile(ss);
		Matcher ma = null;
		ma = pa.matcher(html);
		int arrayLength = 0;

		while (ma.find()) {
			arrayLength++;
		}
		String withTag[] = new String[arrayLength];
		String result[] = new String[arrayLength];
		int count = 0;
		// System.out.println(withTag.length);
		ma = pa.matcher(html);
		while (ma.find()) {

			// System.out.println(ma.group());
			withTag[count] = ma.group();
			result[count] = withTag[count].substring(4,
					withTag[count].length() - 5);
			count++;
		}
		return result;
	}%>
		<%
			//获取结果
			List<Document> result = (List) request.getAttribute("searchResult");
			for (int i = 0; i < result.size(); i++) {

				Document document = result.get(i);
				String contentTemp = document.getContent();
				String contentWithHighlight = contentTemp.substring(22,
						contentTemp.length() - 2);

				String highlightWord[] = array_unique(getHighlightWord(contentWithHighlight));
				//高亮
				for (int j = 0; j < highlightWord.length; j++) {
					contentWithHighlight = contentWithHighlight.replace(
							highlightWord[j], "<span style='color:red'>"
									+ highlightWord[j] + "</span>");
				}

				out.print("<a href=\"" + document.getUrl() + "\">"
						+ document.getUrl() + "</a>");
				out.print("<br>" + contentWithHighlight + "<br><br>");
			}
		%>
	
</body>
</html>