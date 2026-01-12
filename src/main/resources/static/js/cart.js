// 장바구니 추가
/*
function addCart(bookId, memberId) {
	if (confirm("장바구니에 도서를 추가하겠습니까?")) {
		document.addCartForm.action = "/BookMarket/cart/book/" + bookId + "/" + memberId;
		document.addCartForm.submit();
	}
}
*/

// 장바구니 개별 삭제
function removeCartOne(bookId, cartId, memberId) {
	if (confirm("장바구니에서 선택한 도서를 삭제하겠습니까?")) {
		document.deleteCartForm.action = "/BookMarket/cart/book/" + bookId + "/" + cartId+ "/" + memberId;
		document.deleteCartForm.submit();
		setTimeout('location.reload(true)', 200);
	}
}

// 장바구니 전체 삭제
/*
function removeCartAll(memberId) {
	if (confirm("장바구니에서 전체 도서를 삭제하겠습니까?")) {
		document.deleteCartAllForm.action = "/BookMarket/cart/" + memberId + "/" + chkedBookIdList[i].value;
		document.deleteCartAllForm.submit();
		setTimeout('location.reload(true)', 200);
	}
}
*/

// 장바구니 선택 삭제
function removeCheckedCartInfo(memberId, cartId) {
	if (confirm("장바구니에서 전체 도서를 삭제하겠습니까?")) {
		let chkedBookIdList = [];
		$('input[name="cartChkOne"]:checked').each(function() {			
		 	 chkedBookIdList.push($(this).val());
		});
		
		if (chkedBookIdList.length > 0) {
			document.deleteCartAllForm.action = "/BookMarket/cart/" + memberId + "/" + cartId + "/" + chkedBookIdList;
			document.deleteCartAllForm.submit();
			setTimeout('location.reload(true)', 200);
		} else {
			alert("선택한 항목이 없습니다.")
		}
	}
}


// 장바구니 선택 주문
function orderCheckedCartInfo(memberId) {
	let chkedBookIdList = [];
	$('input[name="cartChkOne"]:checked').each(function() {			
	 	 chkedBookIdList.push($(this).val());
	});
	
	if (chkedBookIdList.length > 0) {
		location.href =  "/BookMarket/order/" + memberId + "/"  + chkedBookIdList;
		setTimeout('location.reload(true)', 200);
	} else {
		alert("선택한 항목이 없습니다.")
	}
}

document.addEventListener("DOMContentLoaded", function() {
	// 장바구니 개별 수량 변경
	let i_quantity = document.querySelectorAll(".i-quantity");   // 배열
	let btnQuantity = document.querySelectorAll(".btnQuantity"); // 배열
	let cartBookId = document.querySelectorAll(".cartBookId");   // 배열
	let price = document.querySelectorAll(".price");	         // 배열
	let cartId = document.querySelector("#cartId");
	let memberId = document.querySelector("#memberId");
	let pageNum = document.querySelector("#pageNum");
	
	for (let i = 0; i < btnQuantity.length; i++) {
		btnQuantity[i].addEventListener("click", function() {
			location.href = "/BookMarket/cart/book/" + cartBookId[i].value + "/" + cartId.value + "/" 
				+ i_quantity[i].value + "/" + price[i].value + "/" + memberId.value + "?pageNum=" + pageNum.value;
			setTimeout('location.reload(true)', 200);
		})
	}
	// 장바구니 추가 (주문수량도 함께 추가) -> book.html
	let quantity = document.querySelector("#quantity"); 
	let btnCartAdd = document.querySelector("#btnCartAdd");
	let bookId = document.querySelector("#bookId");
	
	if (btnCartAdd != null) {
		btnCartAdd.addEventListener("click", function() {
			document.addCartForm.action = "/BookMarket/cart/book/" + bookId.value + "/" 
				+ memberId.value + "/" + quantity.value;
			document.addCartForm.submit();
		})
	}
	
	let checkAll = document.querySelector(".cartChkAll");
	if (checkAll != null) {
		checkAll.addEventListener('change', function() {
			let checkboxes = document.querySelectorAll("input[name=cartChkOne]");
			checkboxes.forEach(function(checkbox) {
				checkbox.checked = checkAll.checked;
			})
		})
	}
})

/*
$(document).ready(function() {
	$(".cartChkAll").click(function() {
		if ($(".cartChkAll").is(":checked")) $("input[name=cartChkOne]").prop("checked", true);
		else $("input[name=cartChkOne]").prop("checked", false);
	})
	
	$("input[name=cartChkOne]").click(function() {
		let total = $("input[name=cartChkOne]").length;
		let checked = $("input[name=cartChkOne].checked").length;
		
		if (total != checked) $(".cartChkAll").prop("checked", false);
		else $(".cartChkAll").prop("checked", true);
	})
})
*/








