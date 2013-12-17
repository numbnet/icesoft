package org.icemobile.spring.handler.support;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icemobile.application.ByteArrayResource;
import org.icemobile.application.Resource;
import org.icemobile.application.ResourceAdapter;
import org.icemobile.application.ResourceStore;
import org.icemobile.spring.handler.ByteArrayResourceAdapter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.annotation.Transactional;

/**
 * A simple JDBC-based implementation of the {@link ResourceStore} interface.
 * Uses a basic DefaultLobHandler for reading resource blob data. 
 *
  */
public class JdbcResourceStore implements ResourceStore<ByteArrayResource>{

    protected JdbcTemplate jdbcTemplate;

    protected SimpleJdbcInsert insertResource;

    protected ResourceAdapter resourceAdapter = new ByteArrayResourceAdapter();
	
	private static final Log LOG = LogFactory.getLog(JdbcResourceStore.class);
	
	protected LobHandler lobHandler = new DefaultLobHandler();
	
	protected String tableName = "uploads";
	
	/**
	 * Add the resource to the ResourceStore. 
	 * 
	 * @param resource The ByteArrayResource to add to the ResourceStore.
	 * @see ByteArrayResourceStore
	 */
	@Transactional
	public void add(final ByteArrayResource resource)  {
		Number newKey = this.insertResource.executeAndReturnKey(
					createResourceParameterSource(resource));
		resource.setId(newKey.longValue());
		LOG.info("added " + resource);
		this.jdbcTemplate.execute(
		        "INSERT INTO " + tableName + " (resource, content_type, name, token, uuid, last_updated) VALUES (?, ?, ?, ?, ?, ?)",
		        new AbstractLobCreatingPreparedStatementCallback(lobHandler) {                         
		            protected void setValues(PreparedStatement ps, LobCreator lobCreator) 
		                throws SQLException {
		              lobCreator.setBlobAsBytes(ps, 1, resource.getBytes());    
		              ps.setString(2, resource.getContentType());
		              ps.setString(3, resource.getName());
		              ps.setString(4, resource.getToken());
		              ps.setString(5, resource.getUuid());
		              ps.setLong(6, System.currentTimeMillis());
		            }
		        });
	}

	/**
	 * Remove the Resource from the ResourceStore by token and resource name.
	 * The token/name combination should be unique for a resource.
	 * 
	 * @param token The user-specific token, such as the session id.
	 * @param name The name of the resource.
	 */
	public void remove(String token, String name) {
		this.jdbcTemplate
		    .update("DELETE FROM " + tableName + " WHERE token=? AND name=?", 
		            token, name);
		LOG.info("removed resource of token " + token + ", name = " + name);
	}
	
	/**
     * Remove the Resource from the ResourceStore by id.
     * 
     * @param id The unique id of the resource.
     */
    public void remove(long id) {
        this.jdbcTemplate
            .update("DELETE FROM " + tableName + " WHERE id=?", id);
        LOG.info("removed resource of id " + id );
    }

	private MapSqlParameterSource createResourceParameterSource(ByteArrayResource resource) {
		return new MapSqlParameterSource()
			.addValue("id", resource.getId())
			.addValue("resource", resource.getBytes())
			.addValue("content_type", resource.getContentType())
			.addValue("name", resource.getName())
			.addValue("token", resource.getToken())
			.addValue("uuid", resource.getUuid())
			.addValue("last_updated", resource.getLastUpdated());
	}

    /**
     * Remove the Resource from the ResourceStore.
     * 
     * @param resource The ByteArrayResource to remove.
     * @see ByteArrayResource
     */
	public void remove(ByteArrayResource resource) {
        remove(resource.getId());
    }

    /**
     * Remove all resources from the ResourceStore.
     */
	public void clear() {
        this.jdbcTemplate
            .update("DELETE FROM " + tableName);
    }

    /**
     * Get a ByteArrayResource by the unique combination of token and name.
     * 
     * @param token The user-specific token for the resource, such as the session id.
     * @param name The name of the resource.
     * @return The ByteArrayResource from the ResourceStore
     */
	public ByteArrayResource get(String token, String name) {
	    
        final ResourceStore store = this;
        
        LOG.debug("token=" + token + ", name=" + name);
        
        List<ByteArrayResource> resources = this.jdbcTemplate
                .query(
                        "SELECT id, resource, content_type, name, token, uuid, last_updated FROM " + tableName +
                       " WHERE token = ? AND name = ? ORDER BY id DESC",
                		new RowMapper<ByteArrayResource>() {
                            public ByteArrayResource mapRow(ResultSet rs, int row) throws SQLException {
                                ByteArrayResource resource = new ByteArrayResource();
                                resource.setId(rs.getLong("id"));
                                resource.setContentType(rs.getString("content_type"));
                                resource.setBytes(lobHandler.getBlobAsBytes(rs, "resource"));
                                resource.setName(rs.getString("name"));
                                resource.setStore(store);
                                resource.setToken(rs.getString("token"));
                                resource.setUiid(rs.getString("uuid"));
                                resource.setLastUpdated(rs.getLong("last_updated"));
                                return resource;
                            }},
                            token, name);
        if( resources.size() > 1 ){
            LOG.warn("expected single result, but found multiple returning latest: " + resources);
        }
        ByteArrayResource resource = null;
        if( resources.size() > 0 ){
            resource = resources.get(0);
        }
        LOG.info("loaded " + resource);
        return resource;
    }

    /**
     * Retrieve all resources from the request and add them to the ResourceStore.
     * 
     * @param request The HttpServletRequest
     * @param token Then user-specific token, such as the session id
     */
	public Resource[] handleRequest(HttpServletRequest request, String token) {
        Resource[] resources = null;
        if( resourceAdapter != null ){
            resources = resourceAdapter.handleRequest(request);
        }
        if( resources != null ){
            for( Resource resource : resources ){
                resource.setToken(token);
                resource.setStore(this);
                this.add((ByteArrayResource)resource);
            }
        }
        return resources;
    }

	/**
	 * Retrieve the Resource from the InputStream and add it to the ResourceStore.
	 * 
	 * @param is The InputStream containing the Resource.
	 * @param contentType The contentType (mime-type) of the Resource.
	 * @param name The name of the Resource.
	 * @param token The user-specic token, such as the session id.
	 */
    public void handleInputStream(InputStream is, String contentType, String name, String token) {
        if( this.resourceAdapter != null ){
            Resource resource = this.resourceAdapter.handleInputStream(is, contentType);
            resource.setName(name);
            resource.setToken(token);
            this.add((ByteArrayResource)resource);
        }        
    }

    /**
     * Set the ResourceAdapter for the ResourceStore, such as the FileResourceAdapter
     * or ByteArrayResourceAdapter(default). 
     * 
     * @param adapter The ResourceAdapter to handle Resources and define the Resource type.
     * @see ResourceAdapter
     */
    public void setResourceAdaptor(ResourceAdapter adapter) {
        this.resourceAdapter = adapter;        
    }
    
    /**
     * Set the LobHandler for the Resources. The default is DefaultLobHandler, which
     * will work for all SQL-compliant databases. The OracleLobHandler can be used for
     * Oracle databases.
     *  
     * @param lobHandler The LobHandler to use for blob handling.
     * @see LobHandler
     */
    public void setLobHandler(LobHandler lobHandler){
        this.lobHandler = lobHandler;
    }
    
    /**
     * Set the database table name for the resources, default = 'uploads'.
     * The table must use the following DDL:
     * 
     * <pre class="code">
     * CREATE TABLE IF NOT EXISTS uploads (
     *     id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL,
     *     name VARCHAR(60),
     *     resource BLOB,
     *     content_type VARCHAR(60),
     *     token VARCHAR(60),
     *     uuid VARCHAR(32),
     *     last_updated BIGINT
     * );
     * </pre>
     * 
     * @param tableName The name of the resource table. 
     */
    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    /**
     * Get the resource by UUID.
     * 
     * @param the uuid of the resource
     */
    public ByteArrayResource get(String uuid) {
        
        final ResourceStore store = this;
        
        LOG.debug("uuid=" + uuid);
        
        List<ByteArrayResource> resources = this.jdbcTemplate
                .query(
                        "SELECT id, resource, content_type, name, token, uuid, last_updated FROM " + tableName +
                       " WHERE uuid = ?  ORDER BY id DESC",
                        new RowMapper<ByteArrayResource>() {
                            public ByteArrayResource mapRow(ResultSet rs, int row) throws SQLException {
                                ByteArrayResource resource = new ByteArrayResource();
                                resource.setId(rs.getLong("id"));
                                resource.setContentType(rs.getString("content_type"));
                                resource.setBytes(lobHandler.getBlobAsBytes(rs, "resource"));
                                resource.setName(rs.getString("name"));
                                resource.setStore(store);
                                resource.setToken(rs.getString("token"));
                                resource.setUiid(rs.getString("uuid"));
                                resource.setLastUpdated(rs.getLong("last_updated"));
                                return resource;
                            }},
                            uuid);
        if( resources.size() > 1 ){
            LOG.warn("expected single result, but found multiple returning latest: " + resources);
        }
        ByteArrayResource resource = null;
        if( resources.size() > 0 ){
            resource = resources.get(0);
        }
        LOG.info("loaded " + resource);
        return resource;
    }
    
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertResource = new SimpleJdbcInsert(dataSource)
            .withTableName(tableName)
            .usingGeneratedKeyColumns("id");
    }
}
