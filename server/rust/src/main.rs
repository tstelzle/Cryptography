use std::thread;
use std::net::{TcpListener, TcpStream, Shutdown};
use std::io::{Read, Write};
use std::io::{self, BufRead};
use std::fs::File;
use rand::Rng;

use std::sync::{Arc, Mutex};

const N : u8 = 27;

type Symbol = char;
type Alphabet = [Symbol;N as usize];
type Key = Arc<Mutex<u8>>;
type Message = Vec<Symbol>;
type Messages = Vec<Message>;
type Cyphertext = Vec<Symbol>;

const ALPHABET : Alphabet = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',' '];


fn get_message() -> Message {
    let messages : Messages = io::BufReader::new(File::open("../faust").unwrap()).lines().map(|l| l.unwrap().chars().collect()).collect();
    let mut rng = rand::thread_rng();
    let n : usize = rng.gen::<usize>() % messages.len();
    messages[n].clone()
}

fn find_code(symbol : Symbol) -> Option<u8> {
    for (n,a) in ALPHABET.iter().enumerate() {
	if *a == symbol {
	    return Some(n as u8);
	}
    }
    return None;
}

fn encrypt_symbol(symbol : Symbol, key : &Key) -> Symbol {
//     println!("{}", symbol);
    let k = key.lock().unwrap();
    let code = find_code(symbol).unwrap();
    let encrypted_code = (code + *k) % N;
    return ALPHABET[encrypted_code as usize];
}

fn encrypt(m : &Message, key : &Key) -> Cyphertext {
    let cypher = m.iter().map(|s| encrypt_symbol(*s,key)).collect::<Vec<char>>();
	let cypher_string = cypher.iter().cloned().collect::<String>();
    println!("{}", cypher_string);

    return cypher
}

	    

fn handle_client(mut stream: TcpStream, key : Key) {
    let mut data = [0 as u8; 512];
    
    while match stream.read(&mut data) {
	Ok(0) => {
	    false
	}
        Ok(size) => {
	    let message = std::str::from_utf8(&data[0..size]);
	    match message {
		Ok("") => 0,
		Ok("message\r\n") => {
		    println!("{:?}", message);
		    let line = get_message();
		    let mut line_encrypted = encrypt(&line, &key).iter().map(|c| *c as u8).collect::<Vec<u8>>();
		    let mut nl = "\n".as_bytes().to_vec();
		    line_encrypted.append(&mut nl);
		    stream.write(&line_encrypted).unwrap()
		},
		Ok(n) => {
		    println!("{:?}", n);
		    if n.starts_with("key") {
			let (_,num) = n.split_at(4);
			let pot_key : u8 = match num.trim_end().parse() {
			    Ok(k) => k,
			    Err(_) => 0,
			};
			let mut k = key.lock().unwrap();
			if pot_key == *k {
			    let mut rng = rand::thread_rng();
			    let n : u8 = rng.gen::<u8>() % N;
			    *k = n;
			    println!("New key: {}", k);
			    stream.write(b"Correct!\n").unwrap()
			} else {
			    stream.write(b"Wrong!\n").unwrap()
			}
			
		    } else {
			stream.write(b"Unkown Command\n").unwrap()
		    }
		},
		Err(_) => 0,
	    };
            
            true
        },
        Err(_) => {
	    false
        }
    } {}
}

fn main() {
    let listener = TcpListener::bind("0.0.0.0:3333").unwrap();
    let key : Key = Arc::new(Mutex::new(9));
    // accept connections and process them, spawning a new thread for each one
    println!("Server listening on port 3333");
    for stream in listener.incoming() {
        match stream {
            Ok(stream) => {
                println!("New connection: {}", stream.peer_addr().unwrap());
		let k = key.clone();
                thread::spawn(|| {
                    // connection succeeded
                    handle_client(stream, k)
                });
            }
            Err(e) => {
                println!("Error: {}", e);
                /* connection failed */
            }
        }
    }
    // close the socket server
    drop(listener);
}
