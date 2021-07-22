package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnctionBanco;
import model.ModelLogin;

public class DAOUsuarioRepository {

	private Connection connection;

	public DAOUsuarioRepository() {

		connection = SingleConnctionBanco.getConnection();

	}

	public ModelLogin gravaUsuario(ModelLogin objeto) throws Exception {

		if (objeto.isNovo()) {

			String sql = "INSERT INTO model_login(nome, login, senha, email) VALUES (?, ?, ?, ?);";
			PreparedStatement preparedSql = connection.prepareStatement(sql);

			preparedSql.setString(1, objeto.getNome());
			preparedSql.setString(2, objeto.getLogin());
			preparedSql.setString(3, objeto.getSenha());
			preparedSql.setString(4, objeto.getEmail());

			preparedSql.execute();
			connection.commit();

		} else {
			String sql = "UPDATE model_login SET nome=?, login=?, senha=?, email=? WHERE id = " + objeto.getId() + ";";

			PreparedStatement preparedSql = connection.prepareStatement(sql);

			preparedSql.setString(1, objeto.getNome());
			preparedSql.setString(2, objeto.getLogin());
			preparedSql.setString(3, objeto.getSenha());
			preparedSql.setString(4, objeto.getEmail());

			preparedSql.executeUpdate();
			connection.commit();
		}

		return this.consultaUsuario(objeto.getLogin());

	}
	
	public List<ModelLogin> consultaUsuarioList(String nome) throws Exception{
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where upper(nome) like upper(?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1,"%" + nome + "%");
		
		ResultSet resultado = statement.executeQuery();
		
		while(resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
		
	}

	public ModelLogin consultaUsuario(String login) throws Exception {

		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where upper(login) = upper('" + login + "')";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
		}

		return modelLogin;
	}

	public boolean validarLogin(String login) throws Exception {
		String sql = "select count(1) > 0 as existe from model_login where upper(login) = upper('" + login + "');";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resutlado = statement.executeQuery();

		resutlado.next();/* Pra ele entrar nos resultados do sql */
		return resutlado.getBoolean("existe");

	}

	public void deletarUser(String idUser) throws Exception {
		String sql = "DELETE FROM model_login WHERE id = ?;";

		PreparedStatement prepareSql = connection.prepareStatement(sql);

		prepareSql.setLong(1, Long.parseLong(idUser));

		prepareSql.executeUpdate();

		connection.commit();

	}

}
