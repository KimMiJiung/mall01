// book.js

// 상품 주문
document.addEventListener("DOMContentLoaded", function() {
	// 상품 주문
	let memberId = document.querySelector("#memberId");
	let bookId = document.querySelector("#bookId");
	let quantity = document.querySelector("#quantity");
	let btnOrderAdd = document.querySelector("#btnOrderAdd");
	
	if (btnOrderAdd != null) {
		btnOrderAdd.addEventListener("click", function() {
			location.href = "/BookMarket/order/" + memberId.value + "/" + bookId.value + "/" + quantity.value;			
		})
	}
	
	// 라뷰 내용 펼치기, 접기
	let rContent = document.querySelectorAll(".d-r-3");
	let rBtn1 = document.querySelectorAll(".a-r-3");  // 펼치기
	let rBtn2 = document.querySelectorAll(".a-r-4");  // 접기
	let rCount = new Array(rContent.length);

	// 리뷰 내용 펼치기, 접기
	// 한라인 글자수: 66글자(공백포함)
	// 4줄 84 (height + padding) -> 1줄에 31의 height
	for (let i = 0; i < rContent.length; i++) {
		// 글자수 파악
		rCount[i] = 0;
		//setInterval(() => {console.log(rContent[i].textContent.length)}, 100)
		setInterval(() => { rCount[i] = rContent[i].textContent.length}, 100);
		setInterval(() => {
			rCount[i] = rContent[i].textContent.length;
			if (rCount[i] < 66*4) {
				// 4줄, 264글자가 되지 않을 때
				rBtn1[i].style.display = "none";
				rBtn2[i].style.display = "none";
				//let rHeight = Math.floor(rCount[i] % 66); // 66 : 1줄 글자수
				//rContent[i].style.height = rHeight * 21;  // 21 : 1줄 height
			} else {
				// 4줄, 264글자를 초과할 때
				rBtn1[i].addEventListener("click", function() {
					console.log(i + ":" + rCount[i]);
					console.log(rContent[i].offsetHeight);
					let rHeight = Math.floor(rCount[i] % 66); // 66 : 1줄 글자수
					rContent[i].style.height = rHeight * 32;  // 21 : 1줄 height
					rContent[i].style.overflow = "visible";
					rBtn1[i].style.display = "none";
					rBtn2[i].style.display = "inline-block";
				})
				rBtn2[i].addEventListener("click", function() {
					rContent[i].style.height = 104;
					rContent[i].style.overflow = "hidden";
					rBtn2[i].style.display = "none";
					rBtn1[i].style.display = "inline-block";
				})
			 }
		}, 100);
	}
})

// 리뷰 평점 API
$(document).ready(function() {
	$(".my-rating-4").starRating({
	  	initialRating: 5,
	  	disableAfterRate: true,		
	  	totalStars: 5,
	  	starShape: 'rounded',
	  	starSize: 25,
	  	emptyColor: 'lightgray',
	  	hoverColor: 'hotpink',
	  	activeColor: 'crimson',
	  	useGradient: true,
	  	onHover: function(currentIndex, currentRating, $el){
	    	$('.live-rating').text(currentIndex);
			$('.live-rating-2').val(currentIndex);
	  	},
	  	onLeave: function(currentIndex, currentRating, $el){
	    	$('.live-rating').text(currentRating);
			$('.live-rating-2').val(currentIndex);
		}
	});	
})

