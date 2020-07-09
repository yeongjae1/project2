<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="poly.dto.KyoboDTO"%>
<%@ page import="poly.dto.Yes24DTO"%>
<%@ page import="poly.dto.UserInfoDTO"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="poly.util.CmmUtil"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="poly.util.EncryptUtil"%>

<%!private Logger log = Logger.getLogger(this.getClass());%>
<%
	String ss_user_id = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));	
	List<UserInfoDTO> rList = (List<UserInfoDTO>)request.getAttribute("rList");
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
<title>책좀 보렴</title>

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
	$(document)
			.ready(
					function() {

						$('#openBtn').click(function() {
							$('#myModal').modal({
								show : true
							})
						});

						$(document)
								.on(
										{
											'show.bs.modal' : function() {
												var zIndex = 1040 + (10 * $('.modal:visible').length);
												$(this).css('z-index', zIndex);
												setTimeout(
														function() {
															$('.modal-backdrop')
																	.not(
																			'.modal-stack')
																	.css(
																			'z-index',
																			zIndex - 1)
																	.addClass(
																			'modal-stack');
														}, 0);
											},
											'hidden.bs.modal' : function() {
												if ($('.modal:visible').length > 0) {
													// restore the modal-open class to the body element, so that scrolling works
													// properly after de-stacking a modal.
													setTimeout(
															function() {
																$(document.body)
																		.addClass(
																				'modal-open');
															}, 0);
												}
											}
										}, '.modal');
					});

	function doRegUserCheck(f) {

		if (f.user_id.value == "") {
			alert("아이디를 입력하세요.");
			f.user_id.focus();
			return false;
		}

		if (f.user_name.value == "") {
			alert("이름을 입력하세요.");
			f.user_name.focus();
			return false;
		}

		if (f.password.value == "") {
			alert("비밀번호를 입력하세요.");
			f.password.focus();
			return false;
		}

		if (f.password2.value == "") {
			alert("비밀번호확인을 입력하세요.");
			f.password2.focus();
			return false;
		}

		if (f.email.value == "") {
			alert("이메일을 입력하세요.");
			f.email.focus();
			return false;
		}

		if (f.addr1.value == "") {
			alert("주소를 입력하세요.");
			f.addr1.focus();
			return false;
		}

		if (f.addr2.value == "") {
			alert("상세주소를 입력하세요.");
			f.addr2.focus();
			return false;
		}
	}
	function doLoginUserCheck(f) {

		if (f.user_id.value == "") {
			alert("아이디를 입력하세요.");
			f.user_id.focus();
			return false;
		}

		if (f.password.value == "") {
			alert("비밀번호를 입력하세요.");
			f.password.focus();
			return false;
		}

	}
	function doOnload(){
		var user_id = "<%=ss_user_id%>";

		if (user_id != "admin") {
			alert("관리자 외 출입금지");
			top.location.href = "/index.do";

		}

	}
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
	<section class="relative about-banner">
		<div class="overlay overlay-bg"></div>
		<div class="container">
			<div class="row d-flex align-items-center justify-content-center">
				<div class="about-content col-lg-12">
					<h1 class="text-white">회원관리</h1>
					<p class="text-white link-nav"></p>
				</div>
			</div>
		</div>
	</section>
	<!-- #nav-menu-container -->
	<div class="whole-wrap">
		<div class="container">
			<div class="section-top-border">
				<h3 class="mb-30">회원 관리 테이블</h3>
				<div class="progress-table-wrap">
					<div class="progress-table">
						<div class="table-head">
							<div class="serial">선택</div>
							<div class="country">아이디</div>
							<div class="visit">닉네임</div>
							<div class="percentage">이메일</div>							
						</div>
						<%
						for (int i=0; i<rList.size();i++) {
											
						%>
						<div class="table-row">
							<div class="serial"><%out.println(i+1); %></div>
							<div class="country"><%out.println(CmmUtil.nvl(rList.get(i).getUser_id()));%></div>
							<div class="visit"><%out.println(CmmUtil.nvl(rList.get(i).getUser_name()));%></div>
							<div class="percentage"><%out.println(EncryptUtil.decAES128CBC(CmmUtil.nvl(rList.get(i).getEmail())));%></div>							
						</div>
						<%
						}
						%>						
					</div>
				</div>
			</div>
		</div>
	</div>
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
									<li><a href="#">홈</a></li>
									<li><a href="#">공지사항</a></li>
									<li><a href="#">마이페이지</a></li>
									<li><a href="/user/logOut.do">로그아웃</a></li>
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
							<a data-toggle="modal" data-target="#myModal5" class="btn"
								style="color: black">아이디찾기</a> <a data-toggle="modal"
								data-target="#myModal2" class="btn" style="color: black">회원가입</a>
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
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<form class="form-horizontal" method="post" action="/user/chgPW.do">
					<div class="modal-body">

						<div class="form-group">
							<label for="inputEmail3" class="col-sm-2 control-label">Id</label>
							<div class="col-sm-10">
								<input type="text" name="user_id" class="form-control"
									id="inputEmail3"
									placeholder="<%=CmmUtil.nvl(ss_user_id)%>" disabled>
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

				<form class="form-horizontal" method="post"
					action="/user/deleteId.do">
					<div class="form-group">
						<input type="submit" class="btn btn-danger" value="회원 탈퇴">
					</div>
				</form>

			</div>
		</div>
	</div>
	<div class="modal fade" id="myModal5">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					아이디 찾기
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<form class="form-horizontal" method="post" action="/user/findId.do"
					onsubmit="return doLoginUserCheck(this);">
					<div class="modal-body">
						<div class="form-group">
							<label for="inputEmail3" class="col-sm-2 control-label">Id</label>
							<div class="col-sm-10">
								<input type="text" name="email" class="form-control"
									id="inputEmail3" placeholder="Email">
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<a href="#" data-dismiss="modal" class="btn">취소</a> <input
							type="submit" class="btn btn-primary" value="아이디 찾기">
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