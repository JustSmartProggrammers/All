function updateLoginStatus() {
    const loginButton = document.getElementById('loginButton');
    const logoutButton = document.getElementById('logoutButton');
    const mypageButton = document.getElementById('mypageButton');

    if (typeof isLoggedIn === 'function') {
        const loggedIn = isLoggedIn();
        if (loginButton) loginButton.style.display = loggedIn ? 'none' : 'block';
        if (logoutButton) logoutButton.style.display = loggedIn ? 'block' : 'none';
        if (mypageButton) mypageButton.style.display = loggedIn ? 'block' : 'none';
    } else {
        console.error('isLoggedIn function is not defined');
    }
}

function setupNavbar() {
    updateLoginStatus();

    const logoutButton = document.getElementById('logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', function(e) {
            e.preventDefault();
            if (typeof logout === 'function') {
                logout();
                alert('로그아웃되었습니다.');
                updateLoginStatus();
                window.location.href = '/';
            } else {
                console.error('logout function is not defined');
            }
        });
    }
}

// DOM이 완전히 로드된 후 setupNavbar 함수를 실행
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', setupNavbar);
} else {
    setupNavbar();
}