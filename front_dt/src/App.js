import logo from './logo.svg';
import './App.css';

// App.js 또는 적절한 컴포넌트 파일에 작성
import React, { useEffect, useState } from 'react';
import axios from 'axios';

const App = () => {
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    // 백엔드 서버로 GET 요청 보내기
    axios({
      method: "POST",
      url: "http://localhost:8080/api/v1/user/any/signup",
      headers: {
        'Content-Type': 'application/json'
      },
      data: {
        id: "test02",
        pw: "qwer123123",
        email: "test02@naver.com",
        nickname: "test02",
        phone: "010-1234-1234"
      },
      withCredentials: true
    })
      .then(response => {
        // 성공적으로 데이터를 받아왔을 때의 처리
        setData(response.data);
      })
      .catch(error => {
        // 요청 실패 시 에러 처리
        setError(error);
        console.log(error)
      });
  }, []);

  return (
    <div>
      <h1>Axios GET Request Example</h1>
      {error && <p>Error: {error.message}</p>}
      {data ? (
        <div>
          <h2>Data from Backend:</h2>
          <pre>{JSON.stringify(data, null, 2)}</pre>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default App;