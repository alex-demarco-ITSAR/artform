package it.artform.database;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import it.artform.pojos.*;

@Repository(value="MYSQL")
public class JdbcArtformRepository implements ArtformRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Utente findUtente(String username) {
		return jdbcTemplate.queryForObject("SELECT * from utente WHERE username=?", BeanPropertyRowMapper.newInstance(Utente.class), username);
	}
	
	@Override
	public Utente findUtenteByEmail(String email) {
		return jdbcTemplate.queryForObject("SELECT * from utente WHERE email=?", BeanPropertyRowMapper.newInstance(Utente.class), email);
	}
	
	@Override
	public int saveUtente(Utente u) {
		return jdbcTemplate.update("INSERT INTO utente (nome, cognome, username, email, numeroTelefono, password, bio, punteggio, immagineProfiloSrc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[] {u.getNome(), u.getCognome(), u.getUsername(), u.getEmail(), u.getNumeroTelefono(), u.getPassword(), u.getBio(), u.getPunteggio(), u.getImmagineProfiloSrc()});
	}

	@Override
	public int updateUtente(Utente u) {
		return jdbcTemplate.update("UPDATE utente SET nome=?, cognome=?, username=?, email=?, numeroTelefono=?, password=?, bio=?, punteggio=?, immagineProfiloSrc=? WHERE username=?",
				new Object[] {u.getNome(), u.getCognome(), u.getUsername(), u.getEmail(), u.getNumeroTelefono(), u.getPassword(), u.getBio(), u.getPunteggio(), u.getImmagineProfiloSrc(), u.getUsername()});
	}

	@Override
	public int deleteUtente(String username) {
		return jdbcTemplate.update("DELETE FROM utente WHERE username=?", username);
	}
	
	@Override
	public int activateUserNotifications(String username1, String username2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int savePost(Post p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Post findPost(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Post> findPostsByUtente(String username) {
		return jdbcTemplate.query("SELECT * from post WHERE utenteUsername=?", BeanPropertyRowMapper.newInstance(Post.class), username);
	}

	@Override
	public int updatePost(Post p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deletePost(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int saveNotifica(Notifica p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Notifica findNotifica(String username, Date d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Notifica> findNotificheByUtente(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int saveBadge(Badge b) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Badge findBadge(String nome) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Badge> findBadgesByUtente(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int saveCommissione(Commissione c) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Commissione findCommissione(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Commissione> findCommissioniByArtista(String username) {
		return jdbcTemplate.query("SELECT * from commissione WHERE artistaUsername=?", BeanPropertyRowMapper.newInstance(Commissione.class), username);
	}	

}
