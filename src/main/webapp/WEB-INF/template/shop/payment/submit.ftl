<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.payment.submit")} - Powered By SHOP++</title>
<meta name="author" content="SHOP++ Team" />
<meta name="copyright" content="SHOP++" />
</head>
<body onload="javascript: document.forms[0].submit();">
	<form actiona="${url}" method="${method}">
		[#list parameterMap.entrySet() as entry]
			<input type="hidden" name="${entry.key}" value="${entry.value}" />
		[/#list]
	</form>
</body>
</html>