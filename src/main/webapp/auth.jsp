<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Auth</title>

	<style type="text/css">
		.btn_type2 {
      color: #fff;
      cursor: pointer;
      padding: 0.4rem 2rem;
      user-select: none;
      position: relative;
      border-radius: 4px;
      border: 2px solid #313c57;
      background-color: #25365e;
    }
    .btn_type2:hover,
    .btn_type2:active,
    .btn_type2:focus {
      background-color: #313c57;
    }

    body {
      color: #fff;
      display: flex;
      justify-content: center;
      background-color: #172135;
      font-family: sans-serif;
    }

    .box {
      display: flex;
      align-items: center;
      justify-content: center;
    }
	</style>
</head>
<body>

<div class="box">
  <div  style="margin-bottom: 2em;">
    <form method="post" action="app?action=signin">
      Your Name:<input type="text" name="username">
      Password:<input type="text" name="pass">
      <button type="submit" class="btn_type2">
        Войти
      </button>
    </form>
  </div>
  
  <br>
  <br>
  <div>
    <form method="post" action="app?action=signup">
        Your Name:<input type="text" name="username">
        Password:<input type="text" name="pass">
        <button type="submit" class="btn_type2">
          Зарегистрироваться
        </button>
      </form>
  </div>
</div>
  
  
</body>
</html>