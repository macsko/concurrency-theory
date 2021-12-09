Przygotowałem 3 możliwości wywołania programu (klasa Main):
1) Z argumentem "race" - rozwiązanie problemu wyścigu przy pomocy semaforów binarnych.
2) Z argumentem "race-wrong" - problem wyścigu przy zastosowaniu semaforów binarnych z ifami zamiast while'i - do sprawdzenia braku poprawności tego rozwiązania.
3) Z argumentem "restaurant" - testowanie semafora licznikowego, symulując restaurację do której chce wejść 25 gości, a jest tylko 5 stolików.
    Rozwiązanie jest poprawne, mimo tego, że output programu może być czasami mylący. 
    Jest to spowodowane opóźnieniami w printowaniu na standardowe wyjście.

Odpowiedzi do zadań:
1) Wywołanie programu z argumentem "race".

2) Przy zamienieniu instrukcji while na if może pojawić się problem, gdy do zasobu ustawia sie kolejka chętnych wątków. 
    Załóżmy przypadek, że wątek 1 zdekrementował semafor i przy wychodzeniu z funkcji P wywołuje notify do czekających wątków. 
    Wybudzany jest wtedy jeden z czekających wątków w funkcji P (na przykład wątek 2) i idzie on dekrementować semafor, 
    podczas gdy ten semafor już jest ustawiony na 0 przez poprzedni wątek. 
    Powoduje to, że do sekcji krytycznej mogą dostać się dwa wątki w jednym czasie, 
    co w przypadku tego programu będzie skutkowało wynikiem końcowym różnym od 0. 
    
    Przykład takiego rozwiązania jest po wywołaniu programu z argumentem "race-wrong".
    
3) Semafor binarny jest szczególnym przypadkiem semafora ogólnego:
    - Obydwa z nich można inkrementować i dekrementować.
    - W obydwu rozwiązaniach, najmniejszą wartością jest 0, a chcąc w takim stanie zdekrementować semafor, 
        wątek musi czekać aż inny najpierw zinkrementuje ten semafor.
    - W przypadku semafora binarnego, jest jeszcze ograniczenie górne na wartość 1, 
        a w semaforze ogólnym nie ma ograniczenia górnego.
    - Przy pomocy semafora ogólnego można również realizować mechaniki semafora binarnego. 
        Na przykład dostęp do sekcji krytycznej może być realizowany inicjalizujac semafor licznikowy wartością 1 
        i przy wchodzeniu do sekcji dekrementując ten semafor, a przy wychodzeniu inkrementując. 
        Wtedy semafor licznikowy również przyjmuje wartość co najwyżej 1.
