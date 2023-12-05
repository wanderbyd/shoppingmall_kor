document.addEventListener('DOMContentLoaded', function() {
	// 더보기 버튼 이벤트 리스너
	document.querySelector('.btn_open').addEventListener('click', function(e) {
		e.preventDefault(); // 기본 동작 막기

		let classList = document.querySelector('.detailinfo').classList;
		let contentHeight = document.querySelector('.detailinfo > .content').offsetHeight;

		if (classList.contains('showstep2')) {
			// 이미지가 전부 나온 상태에서 더보기 버튼을 누르면 이미지 감추고 감추기 버튼으로 변경
			classList.remove('showstep2');
			document.querySelector('.btn_close').classList.add('hide');
			document.querySelector('.btn_open').classList.remove('hide');
		} else {
			// 이미지가 줄어든 상태에서 더보기 버튼을 누르면 이미지 전부 나오고 감추기 버튼으로 변경
			classList.add('showstep2');
			document.querySelector('.btn_close').classList.remove('hide');
			document.querySelector('.btn_open').classList.add('hide');
		}
	});

	// 감추기 버튼 이벤트 리스너
	document.querySelector('.btn_close').addEventListener('click', function(e) {
		e.preventDefault(); // 기본 동작 막기

		let classList = document.querySelector('.detailinfo').classList;

		// 이미지가 줄어든 상태에서 감추기 버튼을 누르면 이미지 감추고 더보기 버튼으로 변경
		classList.remove('showstep2');
		document.querySelector('.btn_open').classList.remove('hide');
		document.querySelector('.btn_close').classList.add('hide');
	});

	// 컨텐츠 로딩 완료 후 높이 기준으로 클래스 재처리
	window.addEventListener('load', function() {
		let classList = document.querySelector('.detailinfo').classList;
		let contentHeight = document.querySelector('.detailinfo > .content').offsetHeight;

		if (contentHeight <= 300) {
			classList.remove('showstep1');
			document.querySelector('.btn_open').classList.add('hide');
			document.querySelector('.btn_close').classList.add('hide');
		} else {
			// 이미지가 300보다 크면 초기에는 닫기 버튼을 숨기도록 처리
			document.querySelector('.btn_close').classList.add('hide');
		}
	});
});