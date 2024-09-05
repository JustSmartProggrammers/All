// auth.js

function login(id, username) {
    localStorage.setItem('isLoggedIn', 'true');
    localStorage.setItem('userId', id);
    localStorage.setItem('username', username);
    console.log('Login successful. UserID:', id, 'Username:', username);
    if (typeof updateLoginStatus === 'function') {
        try {
            updateLoginStatus();
        } catch (error) {
            console.error('Error updating login status:', error);
        }
    }
}

function isLoggedIn() {
    return localStorage.getItem('isLoggedIn') === 'true';
}

function logout() {
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    console.log('Logout successful');
    updateLoginStatus(); // 로그아웃 후 상태 업데이트
}

function getCurrentUser() {
    if (isLoggedIn()) {
        const userId = localStorage.getItem('userId');
        const username = localStorage.getItem('username');
        if (!userId || userId === 'undefined') {
            console.error('User data is invalid');
            logout(); // 잘못된 데이터가 있으면 로그아웃 처리
            return null;
        }
        console.log('Current user:', { userId, username });
        return {
            userId: userId,
            username: username
        };
    }
    console.log('No user logged in');
    return null;
}

function updateLoginStatus() {
    const loginButton = document.getElementById('loginButton');
    const logoutButton = document.getElementById('logoutButton');
    const mypageButton = document.getElementById('mypageButton');

    const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';

    if (loginButton) loginButton.style.display = isLoggedIn ? 'none' : 'block';
    if (logoutButton) logoutButton.style.display = isLoggedIn ? 'block' : 'none';
    if (mypageButton) mypageButton.style.display = isLoggedIn ? 'block' : 'none';
}

// 페이지 로드 시 로그인 상태 업데이트
document.addEventListener('DOMContentLoaded', updateLoginStatus);