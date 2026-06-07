const API_BASE = "http://localhost:8080/api/game";
let game = null;
let isAiThinking = false;

const boardElement = document.getElementById("board");
const turnText = document.getElementById("turnText");
const statusBadge = document.getElementById("statusBadge");
const thinking = document.getElementById("thinking");
const overlay = document.getElementById("overlay");
const winnerText = document.getElementById("winnerText");

document.getElementById("startBtn").addEventListener("click", startGame);

async function startGame() {
    const symbol = document.getElementById("symbol").value;
    const difficulty = document.getElementById("difficulty").value;

    try {
        const response = await fetch(`${API_BASE}/start?humanSymbol=${symbol}&difficulty=${difficulty}`, { method: "POST" });
        game = await response.json();
        overlay.style.display = "none";
        renderGame();
    } catch (e) {
        console.error(e); // This will tell you the REAL error in the browser console
        alert("Error: " + e.message);
    }
}
function renderGame() {
    if (!game || !game.board) {
        console.error("No game data to render");
        return;
    }

    // Debugging: This will show you exactly what the board looks like in the console
    console.log("Board Structure:", game.board);

    boardElement.innerHTML = "";

    statusBadge.innerText = game.status || "Active";
    turnText.innerText =  "Your turn!" ;

    for (let r = 0; r < 3; r++) {
        for (let c = 0; c < 3; c++) {
            const cell = document.createElement("button");
            cell.className = "cell";

            let value = '';

            // Resilient Data Access:
            if (Array.isArray(game.board[r])) {
                // Case: 2D Array [[],[],[]]
                value = game.board[r][c];
            } else if (typeof game.board[r] === 'string') {
                // Case: Array of Strings ["...", "...", "..."]
                value = game.board[r][c];
            } else {
                // Case: 1D Flat Array [0,1,2,3,4,5,6,7,8]
                value = game.board[r * 3 + c];
            }

            // Render the value
            if (value && value !== '\u0000' && value.trim() !== "") {
                cell.innerText = value;
                cell.classList.add(value === 'X' ? 'x-text' : 'o-text');
                cell.disabled = true;
            }

            cell.addEventListener("click", () => handleMove(r, c));
            boardElement.appendChild(cell);
        }
    }

    if (game.status && game.status !== "Active") {
        showGameOver();
    }
}

async function handleMove(row, col) {
    if (!game || game.status !== "Active" || isAiThinking) return;

    // Human Move
    const response = await fetch(`${API_BASE}/${game.gameId}/move?row=${row}&col=${col}`, { method: "POST" });
    game = await response.json();
    renderGame();

    if (game.status !== "Active") return;

    // Trigger AI Move
    startAiSequence();
}

function startAiSequence() {
    isAiThinking = true;
    thinking.style.display = "flex";
    turnText.innerText = "The AI is calculating...";

    // Disable all cells visually
    document.querySelectorAll('.cell').forEach(c => c.style.cursor = "wait");

    setTimeout(async () => {
        const aiResponse = await fetch(`${API_BASE}/${game.gameId}/ai-move`, { method: "POST" });
        game = await aiResponse.json();

        isAiThinking = false;
        thinking.style.display = "none";
        renderGame();
    }, 1200); // Slightly faster for better feel
}

function showGameOver() {
    overlay.style.display = "flex";
    if (game.status === "Draw") {
        winnerText.innerText = "It's a Draw! 🤝";
    } else {
        winnerText.innerText = `${game.status} Wins! 🎉`;
    }
}

function resetUI() {
    overlay.style.display = "none";
    startGame();
}