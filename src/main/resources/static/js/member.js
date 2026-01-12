// addMember.html
// 주소검색 - KAKAO 주소 API활용
function searchAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            document.getElementById("zipcode").value = data.zonecode;
			document.getElementById("basicAddress").value = data.address; 
        }
    }).open();
}


// updateMember.html
// 회원탈퇴
function deleteMember(memberId) {
	if (confirm("정말 회원을 탈퇴하겠습니까?")) {
		location.href = "/BookMarket/member/delete/" + memberId;
	}
}