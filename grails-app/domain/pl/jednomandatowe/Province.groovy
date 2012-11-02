package pl.jednomandatowe
;

public enum Province {
		DOLNOSLASKIE("dolnośląskie"),KUJAWSKO_POMORSKIE("kujawsko-pomorskie"),
		LUBELSKIE("lubelskie"),LUBUSKIE("lubuskie"),LODZKIE("łódzkie"),MALOPOLSKIE("małopolskie"),
		MAZOWIECKIE("mazowieckie"),OPOLSKIE("opolskie"),PODKARPACKIE("podkarpackie"),
		PODLASKIE("podlaskie"),POMORSKIE("pomorskie"),SLASKIE("śląskie"),SWIETOKRZYSKIE("świętokrzyskie"),
		WARMINSKO_MAZURSKIE("warmińsko-mazurskie"),WIELKOPOLSKIE("wielkopolskie"),
		ZACHODNIOPOMORSKIE("zachodniopomorskie")
		

		String value

		Province(String value) { this.value = value }

		String toString() { value }
		String getKey() { name() }
			
			 static list(){
			  [DOLNOSLASKIE,KUJAWSKO_POMORSKIE,LUBELSKIE,LUBUSKIE,LODZKIE,MALOPOLSKIE,
				  MAZOWIECKIE,OPOLSKIE,PODKARPACKIE,PODLASKIE,POMORSKIE,SLASKIE,SWIETOKRZYSKIE,
				  WARMINSKO_MAZURSKIE,WIELKOPOLSKIE,
				  ZACHODNIOPOMORSKIE]
			 }
	}

