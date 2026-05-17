<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>集中タイマー | DailyKnowly</title>
    <style>
        body {
            font-family: 'Helvetica Neue', Arial, sans-serif;
            background-color: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            transition: background-color 0.5s ease;
        }
        body.study-mode { background-color: #fff5f5; }
        body.break-mode { background-color: #f0faff; }
        .timer-container {
            background: white;
            padding: 50px 40px;
            border-radius: 24px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.08);
            text-align: center;
            width: 100%;
            max-width: 400px;
        }
        .status { font-size: 1.2rem; color: #7f8c8d; font-weight: bold; letter-spacing: 2px; margin-bottom: 10px; text-transform: uppercase; }
        #display { font-size: 84px; font-weight: 200; color: #2c3e50; margin: 20px 0; font-variant-numeric: tabular-nums; }
        .settings { display: flex; justify-content: center; gap: 15px; background: #f8f9fa; padding: 15px; border-radius: 12px; margin-bottom: 30px; }
        .settings div { display: flex; flex-direction: column; font-size: 12px; color: #95a5a6; }
        .settings input { width: 60px; border: 1px solid #ddd; border-radius: 6px; padding: 8px; text-align: center; font-size: 16px; margin-top: 5px; }
        .controls { display: flex; gap: 15px; justify-content: center; }
        button { flex: 1; border: none; padding: 15px; border-radius: 12px; font-size: 16px; font-weight: bold; cursor: pointer; transition: 0.2s; }
        #startBtn { background-color: #3498db; color: white; box-shadow: 0 4px 0 #2980b9; }
        #stopBtn { background-color: #e74c3c; color: white; box-shadow: 0 4px 0 #c0392b; }
        button:disabled { background-color: #dcdde1 !important; box-shadow: none !important; cursor: not-allowed; }
        .reset-btn { background: none; color: #bdc3c7; margin-top: 25px; text-decoration: underline; width: auto; font-weight: normal; }
    </style>
</head>
<body class="study-mode">

<div class="timer-container">
    <a href="HomeServlet" style="text-decoration:none; color:#95a5a6; font-size:14px;">← ホームへ戻る</a>
    <div class="status" id="status">集中タイム</div>
    <div id="display">25:00</div>

    <div class="settings">
        <div>学習 (分)<input type="number" id="studyInput" value="25" min="1" oninput="updateDefaultTime()"></div>
        <div>休憩 (分)<input type="number" id="breakInput" value="5" min="1" oninput="updateDefaultTime()"></div>
    </div>

    <div class="controls">
        <button id="startBtn" onclick="startTimer()">START</button>
        <button id="stopBtn" onclick="stopTimer()" disabled>STOP</button>
    </div>
    <button class="reset-btn" onclick="resetTimer()">タイマーをリセット</button>
    <input type="hidden" id="subjectIdInput" value="1">
</div>

<script>
    let timer;
    let isRunning = false;
    let isStudyMode = true;
    let timeLeft = 25 * 60; // 秒単位
    let totalStudySeconds = 0;

    function updateDisplay() {
        const mins = Math.floor(timeLeft / 60);
        const secs = timeLeft % 60;
        document.getElementById("display").innerText = 
            (mins < 10 ? "0" : "") + mins + ":" + (secs < 10 ? "0" : "") + secs;
    }

    function updateDefaultTime() {
        if (isRunning) return; // 実行中は入力を反映させない
        const studyMins = parseInt(document.getElementById("studyInput").value) || 0;
        const breakMins = parseInt(document.getElementById("breakInput").value) || 0;
        
        timeLeft = (isStudyMode ? studyMins : breakMins) * 60;
        updateDisplay();
    }

    function startTimer() {
        if (isRunning) return;
        isRunning = true;
        document.getElementById("startBtn").disabled = true;
        document.getElementById("stopBtn").disabled = false;

        timer = setInterval(() => {
            if (timeLeft > 0) {
                timeLeft--;
                if (isStudyMode) totalStudySeconds++;
                updateDisplay();
            } else {
                switchMode();
            }
        }, 1000);
    }

    function stopTimer() {
        clearInterval(timer);
        isRunning = false;
        document.getElementById("startBtn").disabled = false;
        document.getElementById("stopBtn").disabled = true;
        
        // 停止したタイミングで一度DBに保存する（任意）
        if (totalStudySeconds > 0) saveStudyData();
    }

    function switchMode() {
        clearInterval(timer);
        isRunning = false;
        isStudyMode = !isStudyMode;
        
        const studyMins = parseInt(document.getElementById("studyInput").value);
        const breakMins = parseInt(document.getElementById("breakInput").value);
        
        timeLeft = (isStudyMode ? studyMins : breakMins) * 60;
        
        // UI更新
        const statusText = document.getElementById("status");
        if (isStudyMode) {
            document.body.className = "study-mode";
            statusText.innerText = "集中タイム";
            statusText.style.color = "#e74c3c";
            alert("休憩終了！学習を開始しましょう。");
        } else {
            document.body.className = "break-mode";
            const statusText = document.getElementById("status");
            statusText.innerText = "休憩タイム";
            statusText.style.color = "#3498db";
            
            // 🌟まず先にアラートを出して、ユーザーが「OK」を押してフリーズが解けるのを待つ
            alert("お疲れ様！休憩に入ります。"); 
            
            // 🌟画面が完全に安定した「後」に、安全にRenderへデータを送信する！
            saveStudyData(); 
        }
        
        updateDisplay();
        startTimer(); // 自動で次のモードを開始
    }

    function resetTimer() {
        stopTimer();
        isStudyMode = true;
        document.body.className = "study-mode";
        document.getElementById("status").innerText = "集中タイム";
        document.getElementById("status").style.color = "#7f8c8d";
        updateDefaultTime();
    }

    function saveStudyData() {
        // 1分以上動いているか判定（テスト時はここを totalStudySeconds >= 0 にするとすぐ送れます）
        const studyMinutes = Math.floor(totalStudySeconds / 60);
        if (studyMinutes < 1) return; 

        const subjectId = document.getElementById("subjectIdInput").value;
        
        // 🌟【最重要修正】宛先を「StudyTimerServlet」ではなく「StudyServlet」に修正します！
        const targetUrl = "${pageContext.request.contextPath}/StudyServlet";
        
        // Java側が待っているパラメータ「action=timer_record」をセット
        const params = "action=timer_record&subjectId=" + subjectId + "&studyTime=" + totalStudySeconds;

        console.log("👉 正しい宛先に送信します:", targetUrl, "データ:", params);

        fetch(targetUrl, {
            method: "POST",
            body: params,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        }).then(response => {
            if (response.ok) {
                console.log("🎉 Aivenデータベースへの保存に成功しました！");
                totalStudySeconds = 0; // タイマーの記録をリセット
            } else {
                console.error("❌ サーバーに届きましたがエラーが返りました。ステータス:", response.status);
            }
        }).catch(error => {
            console.error("❌ 通信そのものに失敗しました:", error);
        });
    }
</script>
</body>
</html>