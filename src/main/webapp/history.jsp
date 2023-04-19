<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
  <title>VK API Parser</title>

  <style>
    body {
      background: #172135;
      font-size: 24px;
      color: #ffffff;
      font-family: sans-serif;
    }
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
    form {
      margin-bottom: 10px;
    }

    a {
      color: #313c57;
      text-decoration: none;
    }

    li {
      overflow: auto;
      float: none;
      margin: 0;
      padding: 0;
      display: block;
    }
    
    li {
      margin: 0;
      width: 100%;
      position: relative;
      line-height: 32px;
      border: 1px solid #313c57;
    }

  </style>

</head>
<body>
  ${userlist}

  <form method="post" action="app?action=update">
    <button type="submit" class="btn_type2">
      Назад
    </button>
  </form>
  <form method="post" action="app?action=updateclear">
    <button type="submit" class="btn_type2">
      Очистить историю
    </button>
  </form>

</body>
</html>