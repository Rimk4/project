<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
  <head>
    <title>Профиль</title>

    <style type="text/css">
      body {
        color: #fff;
        display: flex;
        justify-content: center;
        background-color: #172135;
        font-family: sans-serif;
      }

      .container {
        width: 60%;
        align-content: center;
      }
      h1 {
        text-align: center;
        border-radius: 6px;
        font-size: 24px;/**/
        line-height: 48px;
        background: #25365e;
      }

      table, td {
        border-bottom: 2px solid #313c57;
        border-collapse: collapse;
      }
      tr {
        line-height: 32px;
      }
      table {
        width: 100%;
      }
      .field {
        width: 25%;
      }

      .button {
        text-decoration: none;
      }
      #button {
        margin: 32px 0px;
      }

      ul {
        padding-inline-start: 0;
      }
      #menu
      {
        list-style-type: none;
        background: #25365e;
        display: flex;
      }

      #menu>li
      {
        margin: 0;
        width: 100%;
        position: relative;
        line-height: 32px;
        border: 1px solid #313c57;
      }

      #menu a 
      {
        height: 25px;
        color: #fff;
        text-decoration: none;
        text-shadow: 0 1px 0 #000;
      }

      /*all categories text color when hover*/
      #menu li:hover > a
      {
        color: #999;
      }

      *html #menu li a:hover /* IE6 */
      {
        color: #313c57;
      }

      #menu li:hover>ul
      {
        display: block;
      }

      /* Sub-menu (second categories)*/
      #menu ul
      {
        list-style: none;
        margin: 0;
        padding: 0;    
        display: none;
        position: absolute;
        top: calc(32px + 1px); /*#menu>li line-height:32px + 1px - border*/
        left: 0;
        width: 100%;
      }

      #menu ul>li
      {
        overflow: auto;
        float: none;
        margin: 0;
        padding: 0;
        display: block;
      }
      .sub2:nth-child(odd),.sub3:nth-child(odd) {
        background: #25365e;
      }
      .sub2:nth-child(even),.sub3:nth-child(even) {
        background: #313c57;
      }
      #menu ul a
      {
        padding: 10px;
        height: auto;
        line-height: 1;
        display: block;
      }

      #menu ul ul
      {
        width: 100%;
        top:0;
        left: -100%;
      }

      li > a:only-child:after { content: ''; }
    </style>

    <style type="text/css">

      @import url('https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css');
      .button {
        color: #fff;
        cursor: pointer;
        padding: 1rem 3rem;
        user-select: none;
        position: relative;
        border-radius: 4px;
        background-color: #25365e;
      }
      .button:hover {
        background-color: #313c57;
      }
      .button:active,
      .button:focus {
        background-color: #313c57;
      }
      .button::before {
        left: 0;
        z-index: 2;
        opacity: 0;
        width: 100%;
        visibility: hidden;
        text-align: center;
        position: absolute;
        content: "\f110";
        transition: opacity 1s;
        font-family: FontAwesome;
        animation: loading-animation 1s linear;
        animation-iteration-count: infinite;
        animation-delay: 1s;
      }
      .button::after {
        top: 0;
        left: 0;
        opacity: 0;
        z-index: 1;
        content: "";
        width: 100%;
        height: 100%;
        visibility: hidden;
        position: absolute;
        transition: opacity 0.5s;
        border-radius: 4px;
        background-color: #25365e;
      }
      .button-controller {
        display: none;
      }
      .button-controller:checked ~ .button::before,
      .button-controller:checked ~ .button::after {
        opacity: 1;
        visibility: visible;
      }
      @-moz-keyframes loading-animation {
        0% {
          opacity: 1;
          transform: rotate(0deg);
        }
        50% {
          opacity: 0.1;
        }
        80% {
          opacity: 1;
        }
        100% {
          transform: rotate(359deg);
        }
      }
      @-webkit-keyframes loading-animation {
        0% {
          opacity: 1;
          transform: rotate(0deg);
        }
        50% {
          opacity: 0.1;
        }
        80% {
          opacity: 1;
        }
        100% {
          transform: rotate(359deg);
        }
      }
      @-o-keyframes loading-animation {
        0% {
          opacity: 1;
          transform: rotate(0deg);
        }
        50% {
          opacity: 0.1;
        }
        80% {
          opacity: 1;
        }
        100% {
          transform: rotate(359deg);
        }
      }
      @keyframes loading-animation {
        0% {
          opacity: 1;
          transform: rotate(0deg);
        }
        50% {
          opacity: 0.1;
        }
        80% {
          opacity: 1;
        }
        100% {
          transform: rotate(359deg);
        }
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
      .save-btn {
        margin: 1rem 2rem;
      }
    </style>
  </head>
  <body>
    <div class="container">
      <div class="row">
        <h1>Информация о человеке</h1>
      </div>
      
      <table>
        <tr>
          <td class="field">ID:</td>
          <td id="id">${user.id}</td>
        </tr>
        <tr>
          <td class="field">Имя:</td>
          <td>${user.name}</td>
        </tr>
        <tr>
          <td class="field">Город:</td>
          <td>${user.city}</td>
        </tr>
        <tr>
          <td class="field">Работа:</td>
          <td>${user.work}</td>
        </tr>
        <tr>
          <td class="field">Университет:</td>
          <td>${user.university}</td>
        </tr>
        <tr>
          <td class="field">Школа:</td>
          <td>${user.school}</td>
        </tr>
        <tr>
          <td class="field">Тип профиля:</td>
          <td>${user.type}</td>
        </tr>
      </table>

      <div id="button">
        <input class="button-controller" id="button" type="checkbox">

        <a href="app?action=update" tabindex="0" for="button" class="button">
          Обновить
        </a>
      </div>

      <ul id="menu">
        <li>
          <center><a href="#">Группы(${gcount})</a></center>
          <ul>
            ${groups}
          </ul>        
        </li>

        <li>
          <center><a href="#">Друзья(${fcount})</a></center>
          <ul>
            ${friends}
          </ul>        
        </li>
            
        <li>
          <center><a href="#">Совпадения</a></center>
          <ul>
            ${commongroups}
            ${commoncity}
            ${commonwork}
            ${commonuniversity}
            ${commonschool}
          </ul>
        </li>
      </ul>
    </div>

    <div class="save-btn">
      <button class="btn_type2">
      </button>
      <div id="data" style="display: none;">${data}</div>
    </div>
  </body>
  <script>
    const sub2s = document.querySelectorAll(".sub2");
    sub2s.forEach(x => x.addEventListener("mouseenter", (e) => {
      e.target.getElementsByTagName("ul")[0].style.top = 36*Array.prototype.slice.call(e.target.parentNode.children).indexOf(e.target) + "px";
    }));
  </script>
  <script>
    var text = document.querySelector("#data").innerHTML;
    console.log(text);
    let ID = document.querySelector("#id").innerHTML;
    let but = document.querySelector(".btn_type2");
  but.innerHTML = (
        '<a style="text-decoration: none;color: #fff;" href="data:text/plain;charset=utf-8,%EF%BB%BF' + encodeURIComponent(text) + '" download="' + ID + '.csv">Сохранить данные</a>'
    )
  </script>
</html>