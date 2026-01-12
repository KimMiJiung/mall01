// books.js
// 상단 : 최신상품 (줄판일 기준으로)
$(document).ready(() => {
      // bxSlider에 들어가 이미지 생성
      $(".slider1").bxSlider({
          slideWidth: 247,           // 슬라이드의 너비
          slideHeder: 460,           // 슬라이드의 높이
          maxSlides: 5,              // 최대 노출 개수
          minSlides: 2,              // 최소 노출 개수
          slideMargin: 20,           // 슬라이드의 좌우 마진
          moveSlides: 1,             // 슬라이드 이동개수
          auto: true,                // 자동 화면전환 여부: 기본값(flase)
          speed: 1000,               // 화면이동 시간
          pause: 4000,               // 화면전환 시간: 이동(1초) + 지연(3초) = 4초
          controls: true,            // 이전, 다음 버튼 유무: 기본값(true)
          autoControls: false,       // 재생, 정지 버튼 유무: 기본값(false)
          pager: true,               // 블릿의 유무: 기본값(true)
          autoHover: true,           // 마우스를 올렸을 때 화면전환 여부, 기본값(false)
          stopAutoOnClick: false,     //  블릿을 클릭했을 때 화면 전환 여부,기본값(false)
          infiniteLoop: true,        // 화면전환 무한루프 여부: 기본값(true)
          // 추가 옵션
          mode: "horizontal",        // 화면전환 방향: horizontal(가로, 기본값), vertical(세로)
          autoDirection: "next",     // 화면전환 진행 방향: next(기본값, 오른쪽에서 왼쪽으로 이동), previous(왼쪽에서 오른쪽으로 이동)
          startSlider: 0,            // 슬라이드 시작 인덱스: 기본값(0), 인덱스는 0번부터 시작
          randomStart: false,         // 슬라이드 시작 번호 랜덤 여부: 기본값(flase)
      })
	  
	  // slider슬라이더 설정
	  $(".slider2").slick( {
	      slidesToShow: 5,          // 슬라이드 노출 개수
	      slidesToScroll: 2,        // 슬라이드 이동 개수
	      autoplay: true,           // 자동 화면전환 여부: 기본값(false)
	      dots: true,               // 블릿의 유무: 기본값(false)
	      speed: 1000,              // 화면전환 시간: 1초
	      autoplaySpeed: 2000,      // 화면지연 시간: 1초
	      pauseOnHover: true,       // 마우스를 올렸을 대 화면전환 여부: 기본값(true)
	      draggable: true,          // 승라이드 드래그 여부: 기본값(true)
	      infinite: true,           // 화면전환 무한반복 여부: 기본값(true)
	      centerMode: true,         // 가운데 정렬 여부: 기본값(false)
	      // 추가 옵션 
	      vertical: false,          // 화면전환 방향: 기본값(false)
	      arrows: false,            // 이전, 다음 버튼 유무 : 기본값(false)  -> 버튼은 커스터마이징해서 사용함
	      fade: false,               // 페이드 아웃 효과 여부: 기본값(false)  -> 슬라이드가 1장으로 바뀐 후 슬라이드 효과 적용
	  });
	  

  })