
# CHANGELOG

## Unreleased

### Added
- Controller tests using `@WebMvcTest` for Reward and Transaction endpoints.
- Endpoint documentation with examples and error payloads in README.

### Changed
- Moved service implementations from `com.api.customer.service.impl` to `com.api.customer.serviceImpl`.
- Replaced wildcard imports with explicit imports.
- Added Javadoc to public classes and methods.
- Reformatted Java sources for consistent spacing.
- Updated tests to deterministic data and new package names.

### Fixed
- Ensured component scanning picks up `serviceImpl` package.
