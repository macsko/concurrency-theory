import re
import itertools
import sys


# Przekształcanie stringa z transakcją na listę ze zmiennymi, gdzie 0. indeks to lewa strona przypisania
def transacts_from_strings(transacts_str):
    # Przyjmujemy, że akcje są oznaczane literami a-z oraz A-Z
    return list(map(lambda l: re.findall(r'[a-zA-Z]', l), transacts_str))


# Generowanie relacji zależności D na podstawie alfabetu i ciągu transakcji
def generate_D(A, transacts):
    # Sprawdzanie zależności między każdą transakcją
    D_half = set([(A[i], A[j]) for i, t in enumerate(transacts)
                  for j, t2 in enumerate(transacts)
                  for a in t2 if t[0] == a])
    # Dopełnienie zbioru D jego symetrią
    return D_half.union(map(lambda d: d[::-1], D_half))


# Generowanie relacji niezależności I jako I = A^2 - D
def generate_I(A, D):
    return set(itertools.product(A, repeat=2)).difference(D)


# Sprawdzanie czy dana akcja a jest zależna od którejś akcji z listy l na podstawie relacji D
def is_dependent_on_list(D, a, l, i):
    if i >= len(l):
        return False
    else:
        if (l[i], a) in D:
            return True
        else:
            return is_dependent_on_list(D, a, l, i + 1)


# Dopisywanie elementu v na koniec listy l
def append(l, v):
    return l + [v]


# Wpisywanie elementu v na i-ty indeks listy l
def insert(l, i, v):
    return l[:i] + [v] + l[i + 1:]


# Znajdowanie indeksu gdzie wstawić daną akcję w liście postaci normalnej Foaty
def fnf_action_index(D, a, result, i):
    # Jeżeli nie była zależna z żadną wcześniejszą akcją to wstawić na indeks 0
    if i == -1:
        return 0
    else:
        # Jeżeli była zależna od i-tej listy w FNF to wstawić na indeks i + 1
        if is_dependent_on_list(D, a, result[i], 0):
            return i + 1
        else:
            return fnf_action_index(D, a, result, i - 1)


# Dodawanie akcji do listy FNF
def add_actions_to_fnf(D, w, i):
    if i < 0:
        return []
    else:
        # Najpierw wygenerowanie wyniku dla wcześniejszych akcji
        result = add_actions_to_fnf(D, w, i - 1)
        # Następnie znalezienie miejsca, gdzie powinno się wstawić obecną akcję,
        # czyli przeglądanie listy FNF od końca i patrzenie na zależności
        idx = fnf_action_index(D, w[i], result, len(result) - 1)
        if len(result) == idx:
            # Dopisywanie nowej grupy z akcją w[i] na koniec FNF
            return append(result, [w[i]])
        else:
            # Dopisywanie akcji w[i] na indeks idx w FNF
            return insert(result, idx, append(result[idx], w[i]))


# Generowanie postaci normalnej Foaty na podstawie relacji zależności D i śladu w
def generate_fnf(D, w):
    return add_actions_to_fnf(D, w, len(w) - 1)


# Zwracanie indeksów akcji ze słowa w, w przypadku gdy są zależne, a w przeciwnym wartość None
def dependent_edge(D, w, i, j):
    if (w[i], w[j]) in D:
        return i, j
    else:
        return None


# Generowanie listy kwawędzi grafu - par zależnych akcji słowa w
def dependent_edges(D, w, i):
    # Chcemy jedynie zależne krawędzie
    return filter(bool, [dependent_edge(D, w, i, j) for j in range(i + 1, len(w))])


# Generowanie grafu Diekerta dla słowa w na podstawie relacji niezależności D
def generate_diekert_graph(D, w):
    # Łączymy listy krawędzi z każdego wierzchołka w jedną listę
    return list(itertools.chain(*[dependent_edges(D, w, i) for i in range(len(w))]))


# Sprawdzanie czy dana krawędź e może być usunięta z grafu G - na podstawie DFSu
def could_be_removed(G, e, i, j):
    if i > j:
        return False
    if i == j:
        return True
    # Sprawdzamy czy którakolwiek krawędź z i-tego wierzchołka kończy się tam gdzie krawędź e
    # Zatem bierzemy krawędzie zaczynające się od i-tego wierzchołka będące różne od e i sprawdzamy dalej
    return any(map(lambda x: could_be_removed(G, e, x[1], j), filter(lambda y: y[0] == i and y != e, G)))


# Minimalizacja grafu Diekerta G
def generate_minimal_graph(G):
    return list(filter(lambda e: not could_be_removed(G, e, e[0], e[1]), G))


# Przekształcanie krawędzi na napis w formacie dot
def edge_str(G, i):
    if i == len(G):
        return ''
    return str(G[i][0] + 1) + ' -> ' + str(G[i][1] + 1) + '\n' + edge_str(G, i + 1)


# Przekształcanie wierzchołka na napis w formacie dot
def label_str(w, i):
    if len(w) == i:
        return ''
    return str(i + 1) + '[label=' + w[i] + ']\n' + label_str(w, i + 1)


# Generowanie grafu G w formacie dot
def generate_dot_graph(G, w):
    return 'digraph g{\n' + edge_str(G, 0) + label_str(w, 0) + '}'


# Generowanie postaci normalnej Foaty na podstawie grafu G
def generate_fnf_from_graph(G, w):
    G_labels = list(map(lambda x: (w[x[0]], w[x[1]]), G))
    # Wystarczy wywołać funkcję jak przy wcześniejszym konstruowaniu FNF,
    # jedynie podmieniając D na krawędzie grafu G
    return add_actions_to_fnf(G_labels, w, len(w) - 1)


if __name__ == '__main__':
    input_filename = sys.argv[1] if len(sys.argv) > 1 else 'input.txt'
    output_filename = sys.argv[2] if len(sys.argv) > 2 else 'output.dot'
    with open(input_filename, 'r') as f:
        lines = f.readlines()
        A = eval(lines[0])
        w = lines[1][:-1]
        transacts_str = lines[2:]

    # Oczytanie transakcji z pliku
    transacts = transacts_from_strings(transacts_str)

    print('Alfabet: \n\tA =', A)
    print('Słowo: \n\tw =', w)

    # Wyznaczanie relacji zależności D
    D = generate_D(A, transacts)
    print('Relacja zależności: \n\tD =', D)

    # Wyzaczanie relacji niezależności I
    I = generate_I(A, D)
    print('Relacja niezależności: \n\tI =', I)

    # Wyznaczanie postaci normalnej Foaty śladu [w]
    FNF = generate_fnf(D, w)
    print('Postać normalna Foaty dla śladu [w]: \n\tFNF =', FNF)

    # Generowanie grafu Diekerta na podstawie relacji zależności
    G = generate_diekert_graph(D, w)
    # Minimalizacja wygenerowanego powyżej grafu Diekerta
    G_min = generate_minimal_graph(G)

    # Generowanie formatu dot dla minimalnego grafu Diekerta
    dot_G = generate_dot_graph(G_min, w)
    print('Minimalnmy graf Diekerta dla słowa w:')
    print(dot_G)

    # Zapisywanie minimalnego grafu Diekerta do pliku output_filename (domyślnie output.dot)
    with open(output_filename, 'w') as f:
        f.write(dot_G)

    # Generowanie postaci normalnej Foaty na podstawie minimalnego grafu Diekerta
    FNF2 = generate_fnf_from_graph(G_min, w)
    print('Postać normalna Foaty na podstawie minimalnego grafu Diekerta: \n\tFNF =', FNF2)
