// Szkielet programu z: https://github.com/balis/conc-phil5

var Fork = function() {
    this.state = 0;
    return this;
}

function getRandomInt(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min)) + min;
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

Fork.prototype.acquire = async function () {
    var n = 1
    while(true) {
        await sleep(getRandomInt(0, 2**n) * 10);

        if(this.state === 0) {
            this.state = 1;
            break;
        }else if(n < 10) {
            n++;
        }
    }
}

async function acquire(f1, f2) {
    var n = 1;
    while(true) {
        await sleep(getRandomInt(0, 2**n) * 10);

        if(f1.state === 0 && f2.state === 0) {
            f1.state = 1;
            f2.state = 1;
            break;
        }else if(n < 10) {
            n++;
        }
    }
}

Fork.prototype.release = function() {
    this.state = 0;
}

var Waiter = function(N, forks) {
    this.N = N;
    this.queue = [];
    this.forks = forks;
    this.permittedPhilosophers = [];
    return this;
}

Waiter.prototype.start = async function() {
    var N = this.N,
        forks = this.forks,
        queue = this.queue,
        permittedPhilosophers = this.permittedPhilosophers;

    while(true) {
        if(permittedPhilosophers.length < Math.floor(N/2)) {
            for(var id of queue) {
                if(forks[id - 1].state === 0 && forks[id%N].state === 0) {
                    permittedPhilosophers.push(id);
                    queue.splice(queue.indexOf(id), 1);
                    break;
                }
            }
        }
        await sleep(0.0001);
    }
}

Waiter.prototype.get = async function(id) {
    var queue = this.queue,
        permittedPhilosophers = this.permittedPhilosophers;
    queue.push(id);
    while(!permittedPhilosophers.includes(id)) {
        await sleep(0.0001);
    }
}

Waiter.prototype.release = function(id) {
    var permittedPhilosophers = this.permittedPhilosophers;
    permittedPhilosophers.splice(permittedPhilosophers.indexOf(id), 1);
}

var Timer = function() {
    this.totalTime = 0;
    this.startTime = new Date().getTime();
    return this;
}

Timer.prototype.start = function() {
    this.startTime = new Date().getTime();
}

Timer.prototype.stop = function() {
    this.totalTime += new Date().getTime() - this.startTime;
}

var Philosopher = function(id, forks) {
    this.id = id + 1;
    this.forks = forks;
    this.f1 = forks[id % forks.length];
    this.f2 = forks[(id+1) % forks.length];
    return this;
}

Philosopher.prototype.eat = async function eat() {
    console.log("Filozof " + this.id + " zaczyna jeść");
    await sleep(eatingTime);
    console.log("Filozof " + this.id + " kończy jeść");
}

Philosopher.prototype.think = async function think() {
    console.log("Filozof " + this.id + " zaczyna myśleć");
    await sleep(thinkingTime);
    console.log("Filozof " + this.id + " kończy myśleć");
}

// Rozwiązanie naiwne - każdy filozof bierze najpierw lewy sztuciec, potem prowy
Philosopher.prototype.startNaive = async function (count) {
    var f1 = this.f1,
        f2 = this.f2;

    var timer = new Timer();
    for(var i = 0; i < count; i++) {
        await f1.acquire();
        await f2.acquire();
        timer.stop();
        await this.eat();
        f1.release();
        f2.release();
        await this.think();
        timer.start();
    }
    return timer.totalTime / iters;
}

// Rozwiązanie asymetryczne - co drugi filozof bierze sztućce w odwrotnej kolejności
Philosopher.prototype.startAsymmetric = async function (count) {
    var f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    var timer = new Timer();
    for(var i = 0; i < count; i++) {
        if(id%2 === 1) {
            await f1.acquire();
            await f2.acquire();
        }else {
            await f2.acquire();
            await f1.acquire();
        }
        timer.stop();
        await this.eat();
        f1.release();
        f2.release();
        await this.think();
        timer.start();
    }
    return timer.totalTime / iters;
}

// Rozwiązanie z kelnerem zarządzajacym sztućcami
Philosopher.prototype.startWaiter = async function (count, waiter) {
    var f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    var timer = new Timer();
    for(var i = 0; i < count; i++) {
        await waiter.get(id);
        await f1.acquire();
        await f2.acquire();
        timer.stop();
        await this.eat();
        f1.release();
        f2.release();
        waiter.release(id);
        await this.think();
        timer.start();
    }
    return timer.totalTime / iters;
}

// Rozwiązanie z możliwym zagłodzeniem
Philosopher.prototype.startStarvation = async function (count) {
    var f1 = this.f1,
        f2 = this.f2;

    var timer = new Timer();
    for (var i = 0; i < count; i++) {
        await acquire(f1, f2);
        timer.stop();
        await this.eat();
        f1.release();
        f2.release();
        await this.think();
        timer.start();
    }
    return timer.totalTime / iters;
}

var args = process.argv.slice(2);
var N = 5;
var iters = 10;
if(args.length > 0) {
    N = parseInt(args[0]);
    if(args.length > 1) {
        iters = parseInt(args[1]);
    }
}

const eatingTime = 1;
const thinkingTime = 1;

var forks = [];
var philosophers = [];
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

var waiter = new Waiter(N, forks);

waiter.start();

var startPromises = [];

for (var i = 0; i < N; i++) {
    switch (args[2]) {
        case "naive":
            startPromises.push(philosophers[i].startNaive(iters));
            break;
        case "asymmetric":
            startPromises.push(philosophers[i].startAsymmetric(iters));
            break;
        case "waiter":
            startPromises.push(philosophers[i].startWaiter(iters, waiter));
            break;
        case "starvation":
            startPromises.push(philosophers[i].startStarvation(iters));
            break;
        default:
            startPromises.push(philosophers[i].startStarvation(iters));
            break;
    }
}

// Wyjście z programu po zakończeniu działania wszystkich filozofów
(async () => {
    var waitingTimes = await Promise.all(startPromises);
    console.log("Filozofowie czekali średnio:");
    for(var i = 0; i < N; i++) {
        console.log(`Filozof ${i + 1}: ${waitingTimes[i]}ms`);
    }
    console.log(`[${waitingTimes.toString()}]`);
    process.exit(0);
})();