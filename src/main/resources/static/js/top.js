// top.html
document.addEventListener("DOMContentLoaded", function() {
	// 상단 - 도서분류별 조회
	const s_category = document.getElementById("s-category");
	s_category.addEventListener("change", function(e) {
		let s_value = e.currentTarget.value;
		console.log()
		location.href ="/BookMarket/books/state?state=category&keyword=" + s_value;
	})
	
	// 상단 : 도서 상태별 조회
	const s_bookCondition = document.getElementById("s-bookCondition");
	s_bookCondition.addEventListener("change", function(e) {
		let s_value = e.currentTarget.value;
		console.log()
		location.href ="/BookMarket/books/state?state=bookCondition&keyword=" + s_value;
	})	
	
	// 메뉴 변경
	const menu_img = document.getElementById("menu-img");
	menu_img.addEventListener("mouseover", function(e) {
		menu_img.src = "/BookMarket/icons/menu2.png";
	})
	
	menu_img.addEventListener("mouseout", function(e) {
		menu_img.src = "/BookMarket/icons/menu1.png";
	})
	
	

})


