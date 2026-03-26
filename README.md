<div align="center">
<img src="https://upload.wikimedia.org/wikipedia/sr/5/57/%D0%A4%D0%9E%D0%9D_%D0%91%D0%B5%D0%BE%D0%B3%D1%80%D0%B0%D0%B4_%28%D0%BB%D0%BE%D0%B3%D0%BE%29.png" alt="FON logo" width="300"/>
<br/>

## **UNIVERZITET U BEOGRADU**  
### **FAKULTET ORGANIZACIONIH NAUKA**

Specijalističke akademske studije  
**Modul: Java tehnologije**

---

### **Seminarski rad iz predmeta: Android programiranje u Javi**

## Softverski sistem za praćenje upotrebe pretkolona u tečnoj hromatografiji  
## Android aplikacija „G-Track“

</div>

---

## 📌 Opis sistema

U okviru seminarskog rada razvijen je softverski sistem za praćenje upotrebe pretkolona u tečnoj hromatografiji.

Aplikacija je namenjena zaposlenima u fizičko-hemijskoj laboratoriji u kontroli kvaliteta, koji obavljaju ispitivanja supstanci ovom metodom. Sistem omogućava pouzdanu i jednostavnu evidenciju korišćenja pretkolona, uz smanjenje mogućnosti greške i povećanje efikasnosti rada.

---

## 🎯 Cilj projekta

Cilj razvoja aplikacije je:

- 📊 unapređenje kontrole laboratorijskih resursa  
- ⏱️ praćenje životnog ciklusa pretkolona  
- ⚠️ pravovremena identifikacija potrebe za zamenom  
- 📉 smanjenje grešaka pri evidenciji  
- 📁 centralizacija podataka i izveštavanje  

---

## ⚙️ Funkcionalnosti sistema

Aplikacija „G-Track“ omogućava:

- 📝 unos podataka o korišćenju pretkolona  
- 🗑️ brisanje evidencije  
- 🔍 pregled i filtriranje podataka po:
  - oznaci pretkolone  
  - korisniku  
  - vremenskom periodu  

- 📊 izvoz podataka u **CSV format**  
- 📧 slanje izveštaja putem email-a  

---

## 📷 QR kod funkcionalnost

Radi pojednostavljenja i ubrzanja procesa evidencije, aplikacija podržava skeniranje **QR koda pretkolone**.

Skeniranjem QR koda automatski se evidentiraju ključni podaci:

- oznaka materijala  
- datum korišćenja  
- korisnik  

### Prednosti:

- ⏱️ ubrzan unos podataka  
- ✅ povećana tačnost  
- 📉 smanjena mogućnost greške  

---

## 🏗️ Arhitektura sistema

Aplikacija je organizovana po slojevima:

- **UI sloj (Activities / Fragments)** – interakcija sa korisnikom  
- **Business logika** – obrada podataka i validacija  
- **Data sloj** – rad sa SQLite bazom  



---


### Karakteristike:

- automatsko kreiranje baze pri prvom pokretanju  
- nema potrebe za dodatnom konfiguracijom  
- lokalno skladištenje podataka  

---

## 🛠️ Tehnologije

- **Java**  
- **Android SDK**  
- **SQLite**  
- **Android Studio**  
- **ZXing / ML Kit (QR skeniranje)**  
- **Java Mail API (email slanje)**  

---

## 🚀 Pokretanje projekta

### 1. Kloniranje repozitorijuma

```bash
git clone https://github.com/zpercinov/G-Track.git
cd G-Track

## 📱 2. Pokretanje u Android Studio okruženju

1. Otvoriti **Android Studio**  
2. Kliknuti na **File → Open**  
3. Izabrati folder projekta  
4. Sačekati **Gradle sync**  
5. Pokrenuti aplikaciju (**Run ▶️**)  
6. Izabrati emulator ili fizički uređaj  

---

## 📊 Izveštavanje

Sistem omogućava:

- generisanje izveštaja o korišćenju  
- filtriranje podataka pre izvoza  
- izvoz u **CSV format**  
- slanje izveštaja putem email-a  

---

## ⚠️ Napomena

Aplikacija je razvijena u edukativne svrhe:

- koristi lokalnu bazu podataka  
- nije namenjena produkcionom okruženju  
- može se dalje unaprediti i proširiti  

---

## 🚀 Moguća unapređenja

- ☁️ integracija sa cloud bazom (Firebase)  
- 🔐 autentikacija korisnika  
- 🔔 notifikacije za zamenu pretkolona  
- 📈 napredna analitika  
- 🌐 integracija sa LIMS sistemima  

---


## 🤝 Saradnja i zahvalnost

Posebnu zahvalnost dugujem:

**Ani D. Trujić**, asistentkinji na predmetu,  
za stručnu podršku, korisne sugestije i posvećenost tokom realizacije projekta.

## 👨‍💻 Autor

**Zoran Perčinov**  
Specijalističke akademske studije – Java tehnologije  
Fakultet organizacionih nauka  
Univerzitet u Beogradu  
📧 zp20244151@student.fon.bg.ac.rs  

---

## 👨‍🏫 Mentor

**prof. Dr Siniša Vlajić**  
Fakultet organizacionih nauka  
Univerzitet u Beogradu  
