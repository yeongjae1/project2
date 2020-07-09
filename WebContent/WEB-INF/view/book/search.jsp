<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page import="poly.dto.KyoboDTO"%>
<%@ page import="poly.dto.Yes24DTO"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="poly.util.CmmUtil"%>
<%@ page import="org.apache.log4j.Logger"%>
 <%
 List<KyoboDTO> rList = (List<KyoboDTO>) request.getAttribute("rList");
 List<Yes24DTO> qList = (List<Yes24DTO>) request.getAttribute("qList");
 String ss_user_id = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
 %>
<!DOCTYPE html>
<html lang="zxx" class="no-js">
<head>
<!-- Mobile Specific Meta -->
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<!-- Favicon-->
<link rel="shortcut icon" href="/img/fav.png">
<!-- Author Meta -->
<meta name="author" content="colorlib">
<!-- Meta Description -->
<meta name="description" content="">
<!-- Meta Keyword -->
<meta name="keywords" content="">
<!-- meta character set -->
<meta charset="UTF-8">
<!-- Site Title -->
<title>검색결과</title>

<link
	href="https://fonts.googleapis.com/css?family=Poppins:100,200,400,300,500,600,700"
	rel="stylesheet">
<!--
			CSS
			============================================= -->
<link rel="stylesheet" href="/css/linearicons.css">
<link rel="stylesheet" href="/css/font-awesome.min.css">
<link rel="stylesheet" href="/css/bootstrap.css">
<link rel="stylesheet" href="/css/magnific-popup.css">
<link rel="stylesheet" href="/css/jquery-ui.css">
<link rel="stylesheet" href="/css/nice-select.css">
<link rel="stylesheet" href="/css/animate.min.css">
<link rel="stylesheet" href="/css/owl.carousel.css">
<link rel="stylesheet" href="/css/main.css">
<script>
        $(document).ready(function() {

            $('#openBtn').click(function() {
                $('#myModal').modal({
                    show: true
                })
            });

            $(document).on({
                'show.bs.modal': function() {
                    var zIndex = 1040 + (10 * $('.modal:visible').length);
                    $(this).css('z-index', zIndex);
                    setTimeout(function() {
                        $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
                    }, 0);
                },
                'hidden.bs.modal': function() {
                    if ($('.modal:visible').length > 0) {
                        // restore the modal-open class to the body element, so that scrolling works
                        // properly after de-stacking a modal.
                        setTimeout(function() {
                            $(document.body).addClass('modal-open');
                        }, 0);
                    }
                }
            }, '.modal');
        });
   </script>
</head>
<body>
	<header id="header">
		<div class="header-top">
			<div class="container">
				<div class="row align-items-center"></div>
			</div>
		</div>
		<div class="container main-menu">
			<div class="row align-items-center justify-content-between d-flex">
				<div id="logo">
					<a href="/index.do" style="color: white">frEEEbook</a>
				</div>
				<nav id="nav-menu-container">
					<ul class="nav-menu">
						<li><a href="/index.do">홈</a></li>
						<li><a href="/notice/NoticeList.do">공지사항</a></li>
						<li><a href="/book/random.do">추천 도서</a></li>
						<li><a href="/book/tts.do">음성지원 도서</a></li>
						<%
							if (ss_user_id.equals("")) {
						%>
						<li><a data-toggle="modal" data-target="#myModal"
							data-backdrop="static" style="color: white">로그인</a></li>
						<%
							} else {
						%>
						<li class="menu-has-children"><a href="">마이페이지</a>
							<ul>
								<li><a data-toggle="modal" data-target="#myModal4"
									data-backdrop="static" style="color: black">내 정보</a></li>
								<li><a href="/user/logOut.do">로그아웃</a></li>
							</ul></li>
						<%
							}
						%>
						<%
							if (ss_user_id.equals("admin")) {
						%>
						<li><a href="/user/manage.do">회원관리</a></li>
						<%
							}
						%>
						<!--if session true logout modal -->
					</ul>
				</nav>
			</div>
		</div>
	</header>
	<!-- #header -->
	<!-- start banner Area -->
	<section class="relative about-banner" id="home">
		<div class="overlay overlay-bg"></div>
		<div class="container">
			<div class="row d-flex align-items-center justify-content-center">
				<div class="about-content col-lg-12">
					<h1 class="text-white">검색 결과</h1>
				</div>
			</div>
		</div>
	</section>
	<!-- End banner Area -->

	<!-- Start Sample Area -->
	<section class="price-area section-gap">
		<div class="container">
			<div class="row d-flex justify-content-center">
				<div class="menu-content pb-70 col-lg-8">
					<div class="title text-center">
						<h1 class="mb-10">검색 결과</h1>

					</div>
				</div>
			</div>
			<div class="row">
				<%
					for (int i = 0; i<rList.size(); i++) {
				%>
				<div class="col-lg-6 col-md-6 ">
					<div class="feature-img">
						<img class="img-fluid"
							src="<%=CmmUtil.nvl(rList.get(i).getImg())%>" alt="">
					</div>
					<a class="posts-title"
						href="<%=CmmUtil.nvl(rList.get(i).getLink())%>">
						<h3><%=CmmUtil.nvl(rList.get(i).getTitle())%></h3>
					</a> <br />
					<div class="user-details row">
						<p class="user-name col-lg-12 col-md-12 col-6">
							<a href="#" onclick="doSomething(); return false;"><%=CmmUtil.nvl(rList.get(i).getAuth())%></a>
							<span class="lnr lnr-user"></span>
						</p>
						<p class="comments col-lg-12 col-md-12 col-6">
							<a href="#" onclick="doSomething(); return false;"><%=CmmUtil.nvl(rList.get(i).getPub())%></a>
							<span class="lnr lnr-bubble"></span>
						</p>
						<p class="date col-lg-12 col-md-12 col-6">
							<a href="#" onclick="doSomething(); return false;"><%=CmmUtil.nvl(rList.get(i).getPubdt())%></a>
							<span class="lnr lnr-calendar-full"></span>
						</p>

					</div>
					<a href="<%=CmmUtil.nvl(rList.get(i).getLink())%>"
						class="primary-btn">상세 링크</a>

				</div>
				<%
					}
				%>
			</div>
			<div class="row">
				<%
					for (int i = 0; i<qList.size(); i++) {
				%>
				<div class="col-lg-6 col-md-6 ">
					<div class="feature-img">
						<img class="img-fluid"
							src="<%=CmmUtil.nvl(qList.get(i).getImg())%>" alt="">
					</div>
					<a class="posts-title"
						href="<%=CmmUtil.nvl(qList.get(i).getLink())%>">
						<h3><%=CmmUtil.nvl(qList.get(i).getTitle())%></h3>
					</a> <br />
					<div class="user-details row">
						<p class="user-name col-lg-12 col-md-12 col-6">
							<a href="#" onclick="doSomething(); return false;"><%=CmmUtil.nvl(qList.get(i).getAuth())%></a>
							<span class="lnr lnr-user"></span>
						</p>
						<p class="comments col-lg-12 col-md-12 col-6">
							<a href="#" onclick="doSomething(); return false;"><%=CmmUtil.nvl(qList.get(i).getPub())%></a>
							<span class="lnr lnr-bubble"></span>
						</p>
						<p class="date col-lg-12 col-md-12 col-6">
							<a href="#" onclick="doSomething(); return false;"><%=CmmUtil.nvl(qList.get(i).getPubdt())%></a>
							<span class="lnr lnr-calendar-full"></span>
						</p>

					</div>
					<a href="<%=CmmUtil.nvl(qList.get(i).getLink())%>"
						class="primary-btn">상세 링크</a>

				</div>
				<%
					}
				%>
			</div>
		</div>
	</section>
	<!-- End Align Area -->

	<!-- start footer Area -->
	<!-- start footer Area -->
	<footer class="footer-area section-gap">
		<div class="container">

			<div class="row">
				<div class="col-lg-6  col-md-6 col-sm-6">
					<div class="single-footer-widget">
						<h6>고객문의</h6>
						<p>wlwhsditmdheoakdhkd@gmail.com</p>
					</div>
				</div>
				<div class="col-lg-6 col-md-6 col-sm-6">
					<div class="single-footer-widget">
						<h6>바로가기</h6>
						<div class="row">
							<div class="col">
								<ul>
									<li><a href="/index.do">홈</a></li>
									<li><a href="/notice/NoticeList.do">공지사항</a></li>
									<li><a href="#">마이페이지</a></li>
								</ul>
							</div>
							<div class="col">
								<ul>
									<li><a href="#">예스24</a></li>
									<li><a href="#">교보문고</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="book_list"></div>
	</footer>
	<!-- End footer Area -->

	<div class="modal fade" id="myModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					로그인
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<form class="form-horizontal" method="post"
					action="/user/getUserLoginCheck.do"
					onsubmit="return doLoginUserCheck(this);">
					<div class="modal-body">

						<div class="form-group">
							<label for="inputEmail3" class="col-sm-2 control-label">Id</label>
							<div class="col-sm-10">
								<input type="text" name="user_id" class="form-control"
									id="inputEmail3" placeholder="Id">
							</div>
						</div>
						<div class="form-group">
							<label for="inputPassword3" class="col-sm-2 control-label">Password</label>
							<div class="col-sm-10">
								<input type="password" name="password" class="form-control"
									id="inputPassword3" placeholder="Password">
							</div>
						</div>
						<div class="text-right">
							<a data-toggle="modal" data-target="#myModal2" class="btn"
								style="color: black">회원가입</a>
						</div>

					</div>
					<div class="modal-footer">
						<a href="#" data-dismiss="modal" class="btn">취소</a> <input
							type="submit" class="btn btn-primary" value="로그인">
					</div>
				</form>
			</div>
		</div>
	</div>
	<div class="modal fade rotate" id="myModal2">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					회원가입
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<form class="form-horizontal" method="post"
					action="/user/insertUserInfo.do"
					onsubmit="return doRegUserCheck(this);">
					<div class="modal-body">

						<div class="form-group">
							<label for="inputEmail3" class="col-sm-2 control-label">Id</label>
							<div class="col-sm-10">
								<input type="text" name="user_id" class="form-control"
									id="inputEmail3" placeholder="Id">
							</div>
						</div>
						<div class="form-group">
							<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
							<div class="col-sm-10">
								<input type="text" name="email" class="form-control"
									id="inputEmail3" placeholder="Email">
							</div>
						</div>
						<div class="form-group">
							<label for="inputEmail3" class="col-sm-2 control-label">Name</label>
							<div class="col-sm-10">
								<input type="text" name="user_name" class="form-control"
									id="inputEmail3" placeholder="Name">
							</div>
						</div>
						<div class="form-group">
							<label for="inputPassword3" class="col-sm-2 control-label">Password</label>
							<div class="col-sm-10">
								<input type="password" name="password" class="form-control"
									id="inputPassword3" placeholder="Password">
							</div>
						</div>
						<div class="form-group">
							<label for="inputPassword3" class="col-sm-2 control-label">Password
							</label>
							<div class="col-sm-10">
								<input type="password" name="password2" class="form-control"
									id="inputPassword3" placeholder="Password Check">
							</div>
						</div>

					</div>
					<div class="modal-footer">
						<a href="#" data-dismiss="modal" class="btn">취소</a> <input
							type="submit" class="btn btn-primary" value="회원가입">
					</div>
				</form>
			</div>
		</div>
	</div>
	<div class="modal fade" id="myModal4">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    내 정보
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title"></h4>
                </div>
                <form class="form-horizontal" method="post"
					action="/user/insertUserInfo.do"
					onsubmit="return doRegUserCheck(this);">
					<div class="modal-body">

						<div class="form-group">
							<label for="inputEmail3" class="col-sm-2 control-label">Id</label>
							<div class="col-sm-10">
								<input type="text" name="user_id" class="form-control"
									id="inputEmail3" placeholder="rDTO.getUserID" disabled>
							</div>
						</div>
						<div class="form-group">
							<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
							<div class="col-sm-10">
								<input type="text" name="email" class="form-control"
									id="inputEmail3" placeholder="rDTO.getEmail" disabled>
							</div>
						</div>
						<div class="form-group">
							<label for="inputEmail3" class="col-sm-2 control-label">Name</label>
							<div class="col-sm-10">
								<input type="text" name="user_name" class="form-control"
									id="inputEmail3" placeholder="Name" disabled>
							</div>
						</div>
						<div class="form-group">
							<label for="inputPassword3" class="col-sm-2 control-label">Password</label>
							<div class="col-sm-10">
								<input type="password" name="password" class="form-control"
									id="inputPassword3" placeholder="Password">
							</div>
						</div>
						<div class="form-group">
							<label for="inputPassword3" class="col-sm-2 control-label">Password
							</label>
							<div class="col-sm-10">
								<input type="password" name="password2" class="form-control"
									id="inputPassword3" placeholder="Password Check">
							</div>
						</div>

					</div>
					<div class="modal-footer">
						<a href="#" data-dismiss="modal" class="btn">취소</a> <input
							type="submit" class="btn btn-primary" value="비밀번호 변경">
					</div>
				</form>
            </div>
        </div>
    </div>

	<script src="/js/vendor/jquery-2.2.4.min.js"></script>
	<script src="/js/popper.min.js"></script>
	<script src="/js/vendor/bootstrap.min.js"></script>
	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBhOdIF3Y9382fqJYt5I_sswSrEw5eihAA"></script>
	<script src="/js/jquery-ui.js"></script>
	<script src="/js/easing.min.js"></script>
	<script src="/js/hoverIntent.js"></script>
	<script src="/js/superfish.min.js"></script>
	<script src="/js/jquery.ajaxchimp.min.js"></script>
	<script src="/js/jquery.magnific-popup.min.js"></script>
	<script src="/js/jquery.nice-select.min.js"></script>
	<script src="/js/owl.carousel.min.js"></script>
	<script src="/js/mail-script.js"></script>
	<script src="/js/main.js"></script>
</body>
</html>